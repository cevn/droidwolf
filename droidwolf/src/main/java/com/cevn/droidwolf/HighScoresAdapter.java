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
            row = vi.inflate(R.layout.listview_item_row_hs, parent, false);
            holder = new HighScoresHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.hsTitle);
            holder.score = (TextView) row.findViewById(R.id.highScore);

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

        return row;
    }

    static class HighScoresHolder
    {
        TextView txtTitle;
        TextView score;
        int id;
    }
}
