package com.cevn.droidwolf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * A map fragment containing a simple view.
 */
public class MyMapFragment extends Fragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {


    private static Dialog d;
    private static final String TAG = "MyMapFragment >";
    private static Circle scentRadius;
    private static Circle killRadius;
    private static ArrayList<Marker> townsList;
    private static WeakHashMap<String, Character> markerCharMap = new WeakHashMap<String, Character>();


    private static View mView;

    private static MapFragment mMapFragment;
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
            //fm.beginTransaction().replace(R.id.map, mMapFragment, "mapfrag").commit();
        }

        if (savedInstanceState == null) {
            // First incarnation of this activity.
            mMapFragment.setRetainInstance(true);
        }else {
            // Reincarnated activity. The obtained map is the same map instance in the previous
            // activity life cycle. There is no need to reinitialize it.
            mMap = mMapFragment.getMap();
        }

        initMap();

        return mView;
    }

    private void initMap() {
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnInfoWindowClickListener(this);


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

    public void updateLocation(Location mLocation, Context context){

        mMap = mMapFragment.getMap();
        Log.v(TAG, "updating map location");
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.5f);
        mMap.animateCamera(cameraUpdate, 4000, null);

        mMap.clear();

        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean werewolf = sp.getBoolean("werewolf", false);

        if (werewolf) {
            scentRadius = mMap.addCircle(new CircleOptions()
                    .radius(200).center(latLng).fillColor(Color.argb(127, 20, 147, 46)).zIndex(0));

            killRadius = mMap.addCircle(new CircleOptions()
                    .radius(50).center(latLng).fillColor(Color.argb(127, 250, 20, 31)).zIndex(1));

                ArrayList<Character> mCharacterList = Character.downloadChars(context);

            for (Character character : mCharacterList) {
                Location charLocation = new Location("Test");
                charLocation.setLatitude(character.getLocation().latitude);
                charLocation.setLongitude(character.getLocation().longitude);

                if (mLocation.distanceTo(charLocation) < 200) {
                    int user_id = Integer.parseInt(sp.getString("user_id", "couldn't find userid"));
                    if (character.getId() != user_id) {
                        Marker mMarker = mMap.addMarker(new MarkerOptions().position(character.getLocation()).title(character.getName()));
                        markerCharMap.put(mMarker.getId(), character);
                        mMarker.showInfoWindow();
                    }

                }
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Character mChar = markerCharMap.get(marker.getId());
        final String id = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_id", "none found");
        final int victim_id = mChar.getId();

        Log.v(TAG, "Marker clicked");
        new AlertDialog.Builder(getActivity())
                .setTitle("Kill " + mChar.getName())
                .setMessage("Are you sure you want to kill " + mChar.getName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", id);
                        jsonObject.addProperty("victimid", victim_id);
                        String baseurl = "https://railswolf.herokuapp.com/users/" ;
                        String user_id = id;
                        String route = "/character/kill";
                        String url = baseurl + user_id + route;
                        Ion.with(getActivity(), url)
                                .setHeader("Content-Type", "application/json")
                                .setHeader("Accept", "application/json")
                                .setJsonObjectBody(jsonObject)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject response) {
                                        if (e != null) e.printStackTrace();
                                        if (response != null) Log.v(TAG, response.toString());
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}