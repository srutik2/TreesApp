package com.example.finalproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.InventoryManager;
import com.example.finalproject.Item;
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
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment {

    /**Map object that fills the fragment. */
    private GoogleMap map;

    /**LatLng pos where camera centers itself (center of main quad).
     * Thinking about changing to user's current location. */
    private static final LatLng CAM_CENTER = new LatLng(40.107551, -88.227277);
    /**Initial zoom of camera. */
    private static final int ZOOM = 16;
    /**How long it takes to zoom camera (ms). */
    private static final int ZOOM_DUR = 16;

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
        return root;
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
