package com.cevn.droidwolf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.MapFragment;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sameer on 12/11/13.
 */
public class MyListFragment extends ListFragment {

    private static final String TAG = "MyListFragment";
    ArrayList<Character> mCharacterList;

    public static MyListFragment newInstance() {
        return new MyListFragment();
    }

    private void downloadChars() {
        SharedPreferences mSharedPrefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String baseUrl = "https://railswolf.herokuapp.com/users/";
        String userid  = mSharedPrefs.getString("user_id", "id not found");
        String suffix  = "/character/show_alive";
        String url = baseUrl + userid + suffix;

        Ion.with(getActivity().getApplicationContext(), url)
                .setHeader("Accept", "application/json")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject response) {
                        if (e != null) e.printStackTrace();

                        if (response != null) {
                            Log.v(TAG, response.toString());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        downloadChars();
        CharacterAdapter mAdapter = new CharacterAdapter(container.getContext(),
                R.layout.listview_item_row,
                mCharacterList);

        ListView charListView = new ListView(getActivity());
        charListView.setAdapter(mAdapter);

        return charListView;
    }
}
