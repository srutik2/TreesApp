package com.example.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

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

                    createExampleMarkers();

                }
            });
        }
        return root;
    }

    /** sets up some example markers for testing. Will be removed. */
    private void createExampleMarkers() {
        new Target(new LatLng(40.109760, -88.228156), "Douglas-Fir", "Pseudotsuga menziesii", R.drawable.ic_pine_tree).setMarker(map, getContext());
        new Target(new LatLng(40.108827, -88.227979), "Eastern White Pine", "Pinus strobus", R.drawable.ic_pine_tree).setMarker(map, getContext());
    }
}
