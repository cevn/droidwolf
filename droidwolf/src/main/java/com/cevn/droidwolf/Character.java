package com.cevn.droidwolf;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sameer on 12/14/13.
 */
public class Character {
    final static String TAG = "Character";
    private int id;
    private int user_id;
    private int votes;
    private String name;
    public boolean werewolf;
    private boolean dead;
    private int score;
    private int max_score;

    private double latitude;
    private double longitude;


    public Character(int id, String name, boolean werewolf) {
        this.id = id;
        this.name = name;
        this.werewolf = werewolf;
    }


    public String getName() {
        return this.name;
    }
    public int getVotes() { return this.votes; }
    public int getId() { return this.id; }
    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }
    public int getScore(){ return this.score; }
    public int getHighScore() {return this.max_score; }

    final static public ArrayList<Character> downloadChars(Context context) {
        final ArrayList<Character> mCharacterList = new ArrayList<Character>();
        SharedPreferences mSharedPrefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String baseUrl = "https://railswolf.herokuapp.com/users/";
        String userid  = mSharedPrefs.getString("user_id", "id not found");
        String suffix  = "/character/show_alive";
        String url = baseUrl + userid + suffix;

        //Log.v(TAG, url);
        JsonArray characters;

        try {
            characters = Ion.with(context, url)
                    .setHeader("Accept", "application/json")
                    .asJsonArray()
                    .get();

            Iterator<JsonElement> iter = characters.iterator();

            while (iter.hasNext()) {
                JsonElement json = iter.next();
                //Log.v(TAG, "json: " + json.toString());
                Character mCharacter = new Gson().fromJson(json, Character.class);
                //Log.v(TAG, "Character name:" +mCharacter.getName());
                mCharacterList.add(mCharacter);
            }
        } catch (Exception e) {e.printStackTrace();}
        return mCharacterList;
    }
}
