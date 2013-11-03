package com.cevn.droidwolf;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {


    private final String TAG = this.getClass().getSimpleName();

    private String mAuthTokenType;
    SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = getPreferences(MODE_PRIVATE);

        String email = mSharedPrefs.getString("email", null);
        String password = mSharedPrefs.getString("password", null);
        String auth_token = mSharedPrefs.getString("auth_token", null);

        if (email == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        if (email != null) {
            if (auth_token == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtra("email",email));
            }
            else if (auth_token != null) {
                Intent mIntent = new Intent(MainActivity.this, DashActivity.class);
                mIntent.putExtra("email", email);
                mIntent.putExtra("password", password);

                startActivity(mIntent);
            }
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
