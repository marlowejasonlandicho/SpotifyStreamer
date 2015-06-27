package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SpotifyStreamerMainActivity extends ActionBarActivity {

    private String SEARCH_ARTIST_FRAGMENT = "SAFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.spotify_streamer_main, new SearchArtistFragment(), SEARCH_ARTIST_FRAGMENT)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SearchArtistFragment searchArtistFragment = (SearchArtistFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_ARTIST_FRAGMENT);
        if (searchArtistFragment != null) {
//            SpotifyStreamerResult.getArtists().addAll(searchArtistFragment.getSearchArtistResultList());
//        EditText editText = (EditText) findViewById(R.id.input_artist_name);
//        q = editText.getText().toString();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        EditText editText = (EditText) findViewById(R.id.input_artist_name);
//        q = editText.getText().toString();
        SearchArtistFragment searchArtistFragment = (SearchArtistFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_ARTIST_FRAGMENT);
        if (searchArtistFragment != null) {
//            searchArtistFragment.updateArtistResult(q);
//            searchArtistFragment.setSearchArtistResultList(SpotifyStreamerResult.getArtists());
        }
//        mLocation = location;
    }

}
