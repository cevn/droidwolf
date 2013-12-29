package com.cevn.droidwolf;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by sameer on 12/19/13.
 */
public class HighScoresFragment extends Fragment{
    private static final String TAG = "HighScoresFragment";

    public static HighScoresFragment newInstance() {
        return new HighScoresFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Character> mCharacterList = Character.downloadChars(getActivity());
        Log.v(TAG, mCharacterList.toString());

        View header = inflater.inflate(R.layout.header_hs, null);
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);

        HighScoresAdapter hsAdapter= new HighScoresAdapter(getActivity(), R.layout.list_row_hs, mCharacterList);
        ListView list = (ListView) view.findViewById(android.R.id.list);

        list.addHeaderView(header);

        list.setAdapter(hsAdapter);
        return view;
    }
}
