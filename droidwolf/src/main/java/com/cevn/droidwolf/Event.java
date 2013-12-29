package com.cevn.droidwolf;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sameer on 12/29/13.
 */
public class Event {
    private int id;
    private String type;
    private int victimid;
    private int killerid;
    private double latitude;
    private double longitude;
    private String created_at;

    public String getType () { return type; }
    public String getCreatedAt () { return created_at; }

    final static public ArrayList<Event> downloadEvents(Context context) {
        final ArrayList<Event> mEventList = new ArrayList<Event>();
        SharedPreferences mSharedPrefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String url = "https://railswolf.herokuapp.com/events/";

        //Log.v(TAG, url);
        JsonArray events;

        try {
            events = Ion.with(context, url)
                    .setHeader("Accept", "application/json")
                    .asJsonArray()
                    .get();

            Iterator<JsonElement> iter = events.iterator();

            while (iter.hasNext()) {
                JsonElement json = iter.next();
                //Log.v(TAG, "json: " + json.toString());
                Event mEvent  = new Gson().fromJson(json, Event.class);
                //Log.v(TAG, "Character name:" +mCharacter.getName());
                mEventList.add(mEvent);
            }
        } catch (Exception e) {e.printStackTrace();}
        return mEventList;
    }
}
