package com.cevn.droidwolf;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sameer on 12/11/13.
 */
public class MyListFragment extends ListFragment {
    private static final String TAG = "MyListFragment";

    public static MyListFragment newInstance() {
        return new MyListFragment();
    }

    private ArrayList<Character> downloadChars() {
        final ArrayList<Character> mCharacterList = new ArrayList<Character>();
        SharedPreferences mSharedPrefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String baseUrl = "https://railswolf.herokuapp.com/users/";
        String userid  = mSharedPrefs.getString("user_id", "id not found");
        String suffix  = "/character/show_alive";
        String url = baseUrl + userid + suffix;

        Log.v(TAG, url);

        Ion.with(getActivity().getApplicationContext(), url)
                .setHeader("Accept", "application/json")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray response) {
                        if (e != null) e.printStackTrace();

                        if (response != null) {
                            Log.v(TAG, response.toString());

                            Iterator<JsonElement> iter = response.iterator();

                            while (iter.hasNext()) {
                                JsonElement json = iter.next();
                                Log.v(TAG, "json: " + json.toString());
                                Character mCharacter = new Gson().fromJson(json, Character.class);
                                mCharacterList.add(mCharacter);
                            }
                        }
                    }
                });

        Log.v(TAG, mCharacterList.toString());
        return mCharacterList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Character> mCharacterList = downloadChars();
        Log.v(TAG, mCharacterList.toString());
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        CharacterAdapter characterAdapter = new CharacterAdapter(getActivity(), R.layout.listview_item_row, mCharacterList);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        String[] items = new String[] {"Item 1", "Item 2", "Item 3"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);

        list.setAdapter(characterAdapter);
        return view;
    }
}
