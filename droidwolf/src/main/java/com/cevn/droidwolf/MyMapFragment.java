package com.cevn.droidwolf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A map fragment containing a simple view.
 */
public class MyMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener,
        LocationListener,
        GoogleMap.OnMapLongClickListener {

    private LocationManager mLocManager;
    private Location mLocation;

    private MapFragment mMapFragment;
    private GoogleMap mMap;

    public MyMapFragment() {
        super();

    }

    public static MyMapFragment newInstance() {
        MyMapFragment fragment = new MyMapFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        FragmentManager fm = getActivity().getFragmentManager();
        mMapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mMapFragment).commit();
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


        return inflater.inflate(R.layout.fragment_map, container, false);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            ViewGroup parentViewGroup = (ViewGroup) mMapFragment.getParentFragment().getView();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    void createMapIfNeeded() {
        if(mMap == null){
            //instantiate map
            mMap = mMapFragment.getMap();
            //check it has been instantiated

            if(mLocManager == null){
                mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                mLocManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        BackgroundLocationService.UPDATE_INTERVAL,
                        10,
                        this);
            }

            //locmanager can return null if no last known locaiton is available.
            mLocation = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


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

                if(mLocation != null){
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 15);
                    mMap.animateCamera(cu);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mMap = mMapFragment.getMap();
            mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
}