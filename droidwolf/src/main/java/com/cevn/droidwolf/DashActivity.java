package com.cevn.droidwolf;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class DashActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Fragment mFragment;
    private String TAG = "DashActivity > ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        checkLocationService();

    }



    private void checkLocationService() {
        SharedPreferences sp = getSharedPreferences("location", MODE_PRIVATE);

        Log.d("CHECK_SERVICE", "Service running: " + (sp.getBoolean("locationService", false) ? "YES" : "NO"));

        if(sp.getBoolean("locationService", false)) return;

        Intent mServiceIntent = new Intent(this, BackgroundLocationService.class);
        startService(mServiceIntent);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Log.v(TAG, "position = " + position);

        switch (position) {
            case 0:
                MyApplication.resumeMap();
                mTitle = getString(R.string.title_section1);
                mFragment = new MyMapFragment().newInstance();
                break;
            case 1:
                MyApplication.pauseMap();
                mTitle = getString(R.string.title_section2);
                mFragment = new MyListFragment().newInstance();

                break;
            case 2:
                MyApplication.pauseMap();
                mTitle = getString(R.string.title_section3);
                mFragment = new MyGameFragment().newInstance();
                break;
            case 3:
                MyApplication.pauseMap();
                mTitle = getString(R.string.title_section4);
                mFragment = new StatusFragment().newInstance();
            case 4:
                MyApplication.pauseMap();
                mTitle = getString(R.string.title_section5);
                mFragment = new HighScoresFragment().newInstance();

        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mFragment)
                .commit();
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.dash, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_signout:
                signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor spedit = sp.edit();
        spedit.remove("remember_token");
        spedit.commit();

        Intent mServiceIntent = new Intent(this, BackgroundLocationService.class);
        stopService(mServiceIntent);

        Intent mIntent = new Intent(this, LoginActivity.class);
        startActivity(mIntent);
    }
}
