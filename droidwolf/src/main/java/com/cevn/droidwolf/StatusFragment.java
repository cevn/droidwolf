package com.cevn.droidwolf;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by sameer on 12/19/13.
 */
public class StatusFragment extends Fragment{
    private static final String TAG = "StatusFragment";

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_char, null);
        ImageView sprite = (ImageView) mView.findViewById(R.id.sprite);
        TextView werewolf = (TextView) mView.findViewById(R.id.werewolf);
        TextView dead = (TextView) mView.findViewById(R.id.dead);
        TextView cur_score = (TextView) mView.findViewById(R.id.cur_score);

        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        if (sp.getBoolean("werewolf", false)) {
            int resId = getResources().getIdentifier("werewolf", "drawable", getActivity().getPackageName());
            sprite.setImageResource(resId);
            werewolf.setText("Werewolf");
        } else {
            int resId = getResources().getIdentifier("townsperson", "drawable", getActivity().getPackageName());
            sprite.setImageResource(resId);
            werewolf.setText("Townsperson");
        }

        if (sp.getBoolean("dead", false)) {
            dead.setText("Status: Dead");
            dead.setTextColor(Color.RED);
        } else {
            dead.setText("Status: Alive");
            dead.setTextColor(Color.GREEN);
        }

        cur_score.setText("Score: " + sp.getString("cur_score", "score not found"));

        return mView;
    }

}
