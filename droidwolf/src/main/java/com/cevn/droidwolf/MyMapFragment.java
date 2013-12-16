package com.cevn.droidwolf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A map fragment containing a simple view.
 */
public class MyMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener{


    private static final String TAG = "MyMapFragment >";
    private static Marker marker;
    private static Circle scentRadius;
    private static Circle killRadius;

    private static ArrayList<Integer> charList;

    private static View mView;

    private MapFragment mMapFragment;
    private static GoogleMap mMap;


    public static MyMapFragment newInstance() {
        MyMapFragment fragment = new MyMapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentManager fm = getActivity().getFragmentManager();
        mMapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) parent.removeView(mView);
        }

        try {
            mView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {}

        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mMapFragment, "mapfrag").commit();
        }

        if (savedInstanceState == null) {
            // First incarnation of this activity.
            mMapFragment.setRetainInstance(true);
        }else {
            // Reincarnated activity. The obtained map is the same map instance in the previous
            // activity life cycle. There is no need to reinitialize it.
            mMap = mMapFragment.getMap();
        }

        createMapIfNeeded();
        return mView;
    }

    void createMapIfNeeded() {
        if(mMap == null){
            //instantiate map
            mMap = mMapFragment.getMap();
            //check it has been instantiated


            //locmanager can return null if no last known locaiton is available.


            if(mMap != null){
                mMap.setOnMapLongClickListener(this);
                mMap.setOnInfoWindowClickListener(this);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.setMyLocationEnabled(true);

                //Manipulate map here (add coordinates/polylines from trip etc etc.)
                UiSettings setting = mMap.getUiSettings();
                setting.setTiltGesturesEnabled(true);
                setting.setRotateGesturesEnabled(true);
                setting.setZoomControlsEnabled(true);
                setting.setMyLocationButtonEnabled(true);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();

        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public static void updateLocation(Location mLocation, Context context){
        Log.v(TAG, "updating map location");
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.5f);

        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean werewolf = sp.getBoolean("werewolf", false);

        if (marker == null) marker = mMap.addMarker(new MarkerOptions().position(latLng));
        else {
            marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(latLng));
        }

        if (werewolf) {

             if (scentRadius == null) {
                 scentRadius = mMap.addCircle(new CircleOptions()
                         .radius(150).center(latLng).fillColor(Color.argb(127, 20, 147, 46)).zIndex(0));
             }
             else {
                 scentRadius.remove();
                 scentRadius = mMap.addCircle(new CircleOptions()
                         .radius(150).center(latLng).fillColor(Color.argb(127, 20, 147, 46)).zIndex(0));

             }

            if (killRadius == null) {
                killRadius = mMap.addCircle(new CircleOptions()
                        .radius(50).center(latLng).fillColor(Color.argb(127, 250, 20, 31)).zIndex(1));
            }
            else {
                killRadius.remove();
                killRadius = mMap.addCircle(new CircleOptions()
                        .radius(50).center(latLng).fillColor(Color.argb(127, 250, 20, 31)).zIndex(1));
            }
        }

        if (!werewolf) {
            ArrayList<Character> mCharacterList = Character.downloadChars(context);

        }

        if (mMap != null) {
            Log.v(TAG, "animating camera");
            mMap.animateCamera(cameraUpdate, 4000, null);
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
}