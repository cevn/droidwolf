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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by sameer on 12/14/13.
 */
public class CharacterAdapter extends ArrayAdapter<Character> {
    private static final String TAG = "CharacterAdapter" ;
    Context context;
    int layoutResourceId;
    ArrayList<Character> characterList = null;

    public CharacterAdapter(Context context, int layoutResourceId, ArrayList<Character> characterList) {
        super(context, layoutResourceId, characterList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.characterList = characterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int newpos = position;
        View row = convertView;
        CharacterHolder holder = null;

        if(row == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = vi.inflate(R.layout.listview_item_row, parent, false);
            holder = new CharacterHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.votes = (TextView) row.findViewById(R.id.charVotes);

            row.setTag(holder);
        }
        else
        {
            holder = (CharacterHolder)row.getTag();
        }

        final Character mChar = characterList.get(position);
        holder.txtTitle.setText(mChar.getName());
        holder.votes.setText(Integer.toString(mChar.getVotes()));
        holder.id = mChar.getId();

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Vote for " + mChar.getName())
                        .setMessage("Are you sure you want to vote to execute " + mChar.getName() + "?")
                        .setPositiveButton("I hate that guy", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
                                                if (response != null) {
                                                    Log.v(TAG, response.toString());
                                                    try {
                                                        String success = response.get("success").toString();
                                                        if (success.equals("true")) {
                                                            Toast.makeText(getContext(),
                                                                    "You voted for " + mChar.getName() + ".", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "You can only vote once per day and during the day.", Toast.LENGTH_LONG).show();
                                                        }
                                                    } catch (Exception f) {
                                                        f.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
                    }
            });

        return row;
    }

    static class CharacterHolder
    {
        TextView txtTitle;
        TextView votes;
        int id;
    }
}