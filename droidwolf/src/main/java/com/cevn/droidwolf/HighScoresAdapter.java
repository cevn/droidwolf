package com.cevn.droidwolf;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by sameer on 12/21/13.
 */
public class HighScoresAdapter extends ArrayAdapter<Character>{
    private static final String TAG = "CharacterAdapter" ;
    Context context;
    int layoutResourceId;
    ArrayList<Character> characterList = null;

    public HighScoresAdapter(Context context, int layoutResourceId, ArrayList<Character> characterList) {
        super(context, layoutResourceId, characterList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.characterList = characterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int newpos = position;
        View row = convertView;
        HighScoresHolder holder = null;

        if(row == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = vi.inflate(R.layout.listview_item_row, parent, false);
            holder = new HighScoresHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.score = (TextView) row.findViewById(R.id.charVotes);

            row.setTag(holder);
        }
        else
        {
            holder = (HighScoresHolder)row.getTag();
        }

        Character mChar = characterList.get(position);
        holder.txtTitle.setText(mChar.getName());
        holder.score.setText(Integer.toString(mChar.getHighScore()));
        holder.id = mChar.getId();

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                final Character mChar2 = characterList.get(newpos);
                jsonObject.addProperty("victimid", mChar2.getId());
                String baseurl = "https://railswolf.herokuapp.com/users/";
                SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                String user_id = sp.getString("user_id", "no id found");
                String rest = "/character/vote";
                String url = baseurl + user_id + rest;
                Ion.with(context, url)
                        .setHeader("Accept", "application/json")
                        .setHeader("Content-Type", "application/json")
                        .setJsonObjectBody(jsonObject)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject response) {
                                if (e != null) e.printStackTrace();
                                Log.v(TAG, response.toString());
                                Log.v(TAG, response.get("success").toString());
                                if (response.get("success").equals("true")) {
                                    Toast.makeText(getContext(), "Voting for" + mChar2.getName() + "succeeded!", Toast.LENGTH_LONG).show();
                                }
                                else if (response.get("success").equals("false")) {
                                    Toast.makeText(getContext(), "You can only vote once per day.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        return row;
    }

    static class HighScoresHolder
    {
        TextView txtTitle;
        TextView score;
        int id;
    }
}
