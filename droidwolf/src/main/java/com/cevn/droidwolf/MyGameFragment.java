package com.cevn.droidwolf;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Fragment which displays the game events.
 */
public class MyGameFragment extends android.app.Fragment {
    private final String TAG = "MyGameFragment";

    public static MyGameFragment newInstance() {
        return new MyGameFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Event> mEventList = Event.downloadEvents(getActivity().getApplicationContext());
        Log.v(TAG, mEventList.toString());

        View header = inflater.inflate(R.layout.header_game, null);
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        EventAdapter eventAdapter = new EventAdapter(getActivity(), R.layout.list_row_game, mEventList);
        ListView list = (ListView) view.findViewById(android.R.id.list);

        list.addHeaderView(header);

        list.setAdapter(eventAdapter);
        return view;
    }
}
