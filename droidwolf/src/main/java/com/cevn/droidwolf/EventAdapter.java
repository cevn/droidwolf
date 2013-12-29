package com.cevn.droidwolf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by sameer on 12/29/13.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "EventAdapter" ;
    Context context;
    int layoutResourceId;
    ArrayList<Event> EventList = null;

    public EventAdapter(Context context, int layoutResourceId, ArrayList<Event> eventList) {
        super(context, layoutResourceId, eventList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.EventList = EventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EventHolder holder = null;

        if(row == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = vi.inflate(R.layout.list_row_game, parent, false);
            holder = new EventHolder();
            holder.eventTitle = (TextView)row.findViewById(R.id.eventTitle);
            holder.timeStamp = (TextView) row.findViewById(R.id.timeStamp);

            row.setTag(holder);
        }
        else
        {
            holder = (EventHolder)row.getTag();
        }

        ArrayList<Event> eventList = Event.downloadEvents(getContext());
        final Event mEvent= eventList.get(position);

        SharedPreferences sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean werewolf = sp.getBoolean("werewolf", false);

        if (mEvent.getType().equals("start")) {
            holder.eventTitle.setText("Game commences.");
        } else if (mEvent.getType().equals("kill")) {
            if (werewolf) {
                holder.eventTitle.setText(mEvent.getVictim() + " was killed by " + mEvent.getKiller() + "!");
            } else {
                holder.eventTitle.setText(mEvent.getVictim() + " was killed by a werewolf!");
            }
        } else if (mEvent.getType().equals("execute")) {
            holder.eventTitle.setText(mEvent.getVictim() + " was executed by popular vote!");
        } else if (mEvent.getType().equals("end")) {
            holder.eventTitle.setText("Game concludes.");
        }
        holder.timeStamp.setText(mEvent.getCreatedAt());


        return row;
    }

    static class EventHolder
    {
        TextView eventTitle;
        TextView timeStamp;
    }
}
