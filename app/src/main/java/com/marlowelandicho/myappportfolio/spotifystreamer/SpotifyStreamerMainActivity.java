package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;


public class SpotifyStreamerMainActivity extends AppCompatActivity implements SearchArtistFragment.OnPopulateResultListener {

    private final String SEARCH_ARTIST_FRAGMENT = "SAFTAG";
    private final String LOG_TAG = SpotifyStreamerMainActivity.class.getSimpleName();
    private Bundle bundle;
    private int REQUEST_CODE = 10;
    //    private SpotifyStreamerResult spotifyStreamerResult = new SpotifyStreamerResult();
    private SpotifyStreamerResult spotifyStreamerResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null && spotifyStreamerResult == null) {
            spotifyStreamerResult = new SpotifyStreamerResult();

            SearchArtistFragment searchArtistFragment = new SearchArtistFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult", spotifyStreamerResult);
            searchArtistFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.spotify_streamer_main, searchArtistFragment, SEARCH_ARTIST_FRAGMENT).addToBackStack(null)
                    .commit();
        } else {
            spotifyStreamerResult = savedInstanceState.getParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult");
        }
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

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult", spotifyStreamerResult);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        SpotifyStreamerResult savedSpotifyStreamerResult = (SpotifyStreamerResult) savedInstanceState.getParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult");
//        if (savedSpotifyStreamerResult != null) {
//            spotifyStreamerResult = savedSpotifyStreamerResult;
//
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle bundle = data.getExtras();
//            spotifyStreamerResult = bundle.getParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult");
//            getSupportFragmentManager().findFragmentByTag(SEARCH_ARTIST_FRAGMENT).getArguments()
//                    .putParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult", spotifyStreamerResult);

            SearchArtistFragment searchArtistFragment = new SearchArtistFragment();
            searchArtistFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.spotify_streamer_main, searchArtistFragment, SEARCH_ARTIST_FRAGMENT).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onPopulateResult(SpotifyStreamerResult spotifyStreamerResult) {
        Intent trackListActivityIntent =
                new Intent(this, TrackListActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult", spotifyStreamerResult);

        trackListActivityIntent.putExtras(bundle);
        startActivityForResult(trackListActivityIntent, REQUEST_CODE);
    }
}