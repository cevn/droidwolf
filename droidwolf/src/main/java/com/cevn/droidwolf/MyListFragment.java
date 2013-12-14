package com.cevn.droidwolf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

/**
 * Created by sameer on 12/11/13.
 */
public class MyListFragment extends android.app.Fragment {
    public static MyListFragment newInstance() {
        return new MyListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentManager fm = getActivity().getFragmentManager();
        Fragment mListFragment = MyListFragment.newInstance();


        return inflater.inflate(R.layout.fragment_list, container, false);}
}
