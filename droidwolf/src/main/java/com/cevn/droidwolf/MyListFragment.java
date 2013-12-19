package com.cevn.droidwolf;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by sameer on 12/11/13.
 */
public class MyListFragment extends ListFragment {
    private static final String TAG = "MyListFragment";

    public static MyListFragment newInstance() {
        return new MyListFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Character> mCharacterList = Character.downloadChars(getActivity());
        Log.v(TAG, mCharacterList.toString());

        View header = inflater.inflate(R.layout.header, null);
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        CharacterAdapter characterAdapter = new CharacterAdapter(getActivity(), R.layout.listview_item_row, mCharacterList);
        ListView list = (ListView) view.findViewById(android.R.id.list);

        list.addHeaderView(header);

        list.setAdapter(characterAdapter);
        return view;
    }
}
