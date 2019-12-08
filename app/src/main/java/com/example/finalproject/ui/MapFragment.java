package com.example.finalproject.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.finalproject.InventoryManager;
import com.example.finalproject.LocationListenerService;
import com.example.finalproject.R;
import com.example.finalproject.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment {

    /**Map object that fills the fragment. */
    private GoogleMap map;

    /**LatLng pos where camera centers itself (center of main quad).
     * Thinking about changing to user's current location. */
    private static final LatLng CAM_CENTER = new LatLng(40.107551, -88.227277);
    /**Initial zoom of camera. */
    private static final int ZOOM = 16;
    /**How long it takes to zoom camera (ms). */
    private static final int ZOOM_DUR = 2500;

    /**Log tag. */
    private static final String TAG = "MapSetup";
    /** The radial location accuracy required to send a location update. */
    private static final float REQUIRED_LOCATION_ACCURACY = 28f;
    /** The handler for location updates sent by the location listener service. */
    private BroadcastReceiver locationUpdateReceiver;
    /** Whether the user's location has been found and used to center the map. */
    private boolean centeredMap;
    /** Whether permission has been granted to access the phone's exact location. */
    private boolean hasLocationPermission;

    /**An Inventory Manager implemented by MainActivity.*/
    private InventoryManager invMan;

    /**All targets on the map.*/
    private List<Target> targets = new ArrayList<>();

    /**Apparently this is required. */
    public MapFragment() {
        // Required empty public constructor
    }

    /**uses the super onCreate function.
     * @param savedInstanceState saved state. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /** Creates root, and sets up the map fragment.
     * @param inflater inflater for getting root from parent.
     * @param container parent container that contains this fragment.
     * @param savedInstanceState unused until I can figure out what it is for.
     * @return the root view.
     */
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        //I want to save the map so it doesn't reset and zoom in each time you start the activity.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    map = googleMap;
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.clear();

                    CameraPosition camPos = CameraPosition.builder().target(CAM_CENTER).zoom(ZOOM).bearing(0).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos), ZOOM_DUR, null);

                    if (hasLocationPermission) {
                        // Can only enable the blue My Location dot if the location permission is granted.
                        map.setMyLocationEnabled(true);
                        Log.i(TAG, "setUpMap enabled My Location");
                    }

                    createMarkers();

                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(final Marker marker) {
                            reward(marker);
                            //said false by default, if stuff starts breaking, change back.
                            return false;
                        }
                    });

                }
            });
        }
        // Set up a receiver for location-update messages from the service (LocationListenerService)
        locationUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                Log.i(TAG, "Received location update from service");
                // Android Intents represent action plans or notifications
                // They can contain data - the ones from our service contain a Location
                Location location = intent.getParcelableExtra(LocationListenerService.UPDATE_DATA_ID);

                // If the location is usable, call updateLocation
                if (map != null && location != null && location.hasAccuracy()
                        && location.getAccuracy() < REQUIRED_LOCATION_ACCURACY) {
                    ensureMapCentered(location);
                    updateLocation(location);
                }
            }
        };
        // Register (activate) it
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(locationUpdateReceiver,
                new IntentFilter(LocationListenerService.UPDATE_ACTION)); // Only listen for messages from the service

        // Android only allows location access to apps that asked for it and had the request approved by the user
        // See if we need to make a request
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission isn't already granted, start a request
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            Log.i(TAG, "Asked for location permission");
            // The result will be delivered to the onRequestPermissionsResult function
        } else {
            Log.i(TAG, "Already had location permission");
            // If we have the location permission, start the location listener service
            hasLocationPermission = true;
            startLocationWatching();
        }
        return root;
    }

    /**Called by the Android system when the activity is stopped and cannot be returned to. */
    @Override
    public void onDestroy() {
        // The "super" call is required for all activities
        super.onDestroy();
        // Location is only needed while playing a game - stop the service to save power
        stopLocationWatching();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(locationUpdateReceiver);
        Log.i(TAG, "Destroyed");
    }

    /**
     * Called by the Android system when a permissions request receives a response from the user.
     * @param requestCode the ID of the request (always 0 in our case)
     * @param permissions the affected permissions' names
     * @param grantResults whether each permission was granted (corresponds to the permissions array)
     */
    @Override
    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions,
                                           final @NonNull int[] grantResults) {
        Log.i(TAG, "Permission request result received");
        // The "super" call is required so that the notification will be delivered to fragments
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check whether the request was approved by the user
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted by the user");
            // Got the location permission for the first time
            hasLocationPermission = true;
            // Enable the My Location blue dot on the map
            if (map != null) {
                Log.i(TAG, "onRequestPermissionsResult enabled My Location");
                map.setMyLocationEnabled(true);
            }
            // Start the location listener service
            startLocationWatching();
            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            final int versionThreshold = 26;
            if (Build.VERSION.SDK_INT >= versionThreshold) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
        }
    }

    /**
     * Centers the map on the user's location if the map hasn't been centered yet.
     * @param location the current location
     */
    private void ensureMapCentered(final Location location) {
        if (location != null && !centeredMap) {
            final float defaultMapZoom = 18f;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), defaultMapZoom));
            centeredMap = true;
            Log.i(TAG, "Centered map");
        }
    }

    /**Called when location is updated.
     *@param location the location from update. */
    private void updateLocation(final Location location) {

    }

    /**Starts watching for location changes if possible under the current permissions. */
    @SuppressWarnings("MissingPermission")
    private void startLocationWatching() {
        Log.i(TAG, "Starting location watching");
        // Make sure the location permission has been granted
        if (!hasLocationPermission) {
            Log.w(TAG, "startLocationWatching: Missing permission");
            return;
        }
        // Make sure the My Location blue dot on the map is enabled
        if (map != null) {
            map.setMyLocationEnabled(true);
            Log.i(TAG, "startLocationWatching enabled My Location");
        }
        // Start the location listener service, which will notify this activity of movements
        ContextCompat.startForegroundService(Objects.requireNonNull(getContext()), new Intent(getContext(), LocationListenerService.class));
        // Keep the screen on even if not touched in a while
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**Stops watching for location changes. */
    private void stopLocationWatching() {
        Log.i(TAG, "Stopping location watching");
        // Stop the location listener service
        Objects.requireNonNull(getActivity()).stopService(new Intent(getContext(), LocationListenerService.class));
        // Allow the screen to turn off after a moment of inactivity
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**Rewards the player based on the target they visit.
     * @param marker A marker that represents the target giving a reward. */
    private void reward(final Marker marker) {
        System.out.println("reward");
        for (Target t : targets) {
            if (t.getMarker().equals(marker)) {
                System.out.println(t.getName());
                invMan.addToInventory(t.reward());
            }
        }
    }

    /** sets up some example markers for testing. Will be removed. */
    private void createMarkers() {
        targets = invMan.getAllTargets();
        for (Target t : targets) {
            t.setMarker(map, getContext());
        }
    }

    /** Pretty sure this is called when this fragment is attached to fragment view in MainActivity.
     * This function's purpose is to create the inventory manager based on MainActivity's implementation.
     * @param context main context */
    @Override
    public void onAttach(@NotNull final Context context) {
        super.onAttach(context);
        try {
            invMan = (InventoryManager) context;
        } catch (ClassCastException castException) {
            System.out.println("The activity does not implement the listener.");
            castException.printStackTrace();
        }
    }
}
