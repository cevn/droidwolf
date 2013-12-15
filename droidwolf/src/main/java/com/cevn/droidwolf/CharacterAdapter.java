package com.cevn.droidwolf;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sameer on 12/14/13.
 */
public class CharacterAdapter extends ArrayAdapter<Character> {
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
        View row = convertView;
        CharacterHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CharacterHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (CharacterHolder)row.getTag();
        }

        Character mChar = characterList.get(position);
        holder.txtTitle.setText(mChar.name);

        return row;
    }

    static class CharacterHolder
    {
        TextView txtTitle;
    }
}