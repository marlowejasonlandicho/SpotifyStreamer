package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SimplePlayerActivity extends AppCompatActivity {
    private static final String SIMPLEPLAYER_ACTIVITY_FRAGMENT = "SPTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);

        Intent playTrackActivityIntent = getIntent();
        SimplePlayerActivityFragment simplePlayerActivityFragment = new SimplePlayerActivityFragment();
        Bundle bundle = (Bundle) playTrackActivityIntent.getExtras();
        simplePlayerActivityFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_play_track, simplePlayerActivityFragment, SIMPLEPLAYER_ACTIVITY_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
