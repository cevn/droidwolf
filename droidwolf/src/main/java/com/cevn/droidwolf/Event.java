package com.cevn.droidwolf;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Convenience class mostly used for deserializing json using Gson.
 * Static method downloadEvents so that you can get a list of events from anywhere.
 */
public class Event {
    private int id;
    private String event_type;
    private String victim;
    private String killer;
    private String created_at;

    private double latitude;
    private double longitude;

    public String getType () { return event_type; }


    /**
     * Parse the postgres created_at field into human readable form and return it.
     * @return date in human readable form.
     */
    public String getCreatedAt () {
        String[] DateTime = created_at.split("T");
        String date = DateTime[0];
        String time = DateTime[1];

        String[] YrMonthDay = date.split("-");
        String[] HourMinuteSecond = time.split(":");

        String month = YrMonthDay[1];
        String day = YrMonthDay[2];

        String hour = HourMinuteSecond[0];
        String minute = HourMinuteSecond[1];

        return month + "/" + day + "   " + hour + ":" + minute;
    }
    public String getVictim () { return victim; }
    public String getKiller() { return killer; }

    final static public ArrayList<Event> downloadEvents(Context context) {
        final ArrayList<Event> mEventList = new ArrayList<Event>();
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
