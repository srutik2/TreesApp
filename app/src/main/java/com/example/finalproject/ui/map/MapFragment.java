package com.example.finalproject.ui.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.finalproject.R;

public class MapFragment extends Fragment {

    private GoogleMap map;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //Goal is to implement one of the following:
    //                  Target class takes a map as a constructor parameter, and creates a marker with whatever other parameters are passed
    //                  OR Target class has a placeMarker(GoogleMap map) function that creates a marker with whatever applicable non-null attributes it has.
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.clear();

                    CameraPosition camPos = CameraPosition.builder().target(new LatLng(40.107551,-88.227277)).zoom(16).bearing(0).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos), 10000, null);

                    addMarker(new LatLng(40.109760,-88.228156), "Tree 1", R.drawable.ic_pine_tree);
                    addMarker(new LatLng(40.108827,-88.227979), "Tree 2", "Eastern white pine");

                }
            });
        }
        return root;
    }

    public void addMarker(final LatLng pos, final String name) {
        map.addMarker(new MarkerOptions().position(pos).title(name));
    }

    private void addMarker(final LatLng pos, final String name, final String desc) {
        map.addMarker(new MarkerOptions().position(pos).title(name).snippet(desc));
    }

    private void addMarker(final LatLng pos, final String name, final int icon) {
        map.addMarker(new MarkerOptions().position(pos).title(name).icon(vectorToBitmap(icon, Color.parseColor("#008577"))));
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = null;
        if (vectorDrawable != null) {
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        assert bitmap != null;
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}