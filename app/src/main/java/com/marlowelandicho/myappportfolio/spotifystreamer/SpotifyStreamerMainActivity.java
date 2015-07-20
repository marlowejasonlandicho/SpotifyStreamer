package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;


public class SpotifyStreamerMainActivity extends AppCompatActivity implements SearchArtistFragment.ResultListener {

    private final String SEARCH_ARTIST_FRAGMENT = "SAFTAG";
    private final String LOG_TAG = SpotifyStreamerMainActivity.class.getSimpleName();
    private int REQUEST_CODE = 10;
    private SpotifyStreamerResult spotifyStreamerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchArtistFragment searchArtistFragment = new SearchArtistFragment();
        Bundle bundle = new Bundle();

        if (savedInstanceState == null && spotifyStreamerResult == null) {
            spotifyStreamerResult = new SpotifyStreamerResult();
            bundle.putParcelable(getString(R.string.spotify_streamer_result), spotifyStreamerResult);

        } else {
            spotifyStreamerResult = savedInstanceState.getParcelable(getString(R.string.spotify_streamer_result));
        }

        searchArtistFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.spotify_streamer_main, searchArtistFragment, SEARCH_ARTIST_FRAGMENT).addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.spotify_streamer_result), spotifyStreamerResult);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SpotifyStreamerResult savedSpotifyStreamerResult = (SpotifyStreamerResult) savedInstanceState.getParcelable(getString(R.string.spotify_streamer_result));
        if (savedSpotifyStreamerResult != null) {
            spotifyStreamerResult = savedSpotifyStreamerResult;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            this.spotifyStreamerResult = bundle.getParcelable(getString(R.string.spotify_streamer_result));
        }
    }

    @Override
    public void populateResult(SpotifyStreamerResult spotifyStreamerResult) {
        Intent trackListActivityIntent =
                new Intent(this, TrackListActivity.class);
//        spotifyStreamerResult.addArtistTopTracks(
//                this.spotifyStreamerResult.getArtistId(),
//                this.spotifyStreamerResult.getArtistTopTracks(this.spotifyStreamerResult.getArtistId()));

        spotifyStreamerResult.addArtistTopTracks(this.spotifyStreamerResult.getArtistTopTracks());

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.spotify_streamer_result), spotifyStreamerResult);

        trackListActivityIntent.putExtras(bundle);
        startActivityForResult(trackListActivityIntent, REQUEST_CODE);
    }
}
