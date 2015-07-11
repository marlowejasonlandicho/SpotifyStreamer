package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;


public class TrackListActivity extends AppCompatActivity implements TrackListActivityFragment.OnPopulateResultListener {

    private static final String LOG_TAG = TrackListActivity.class.getSimpleName();
    private static final String TRACKLIST_ACTIVITY_FRAGMENT = "TAFTAG";
    private SpotifyStreamerResult spotifyStreamerResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_track_search);
        Intent trackListActivityIntent = getIntent();
        TrackListActivityFragment trackListActivityFragment = new TrackListActivityFragment();
        Bundle bundle = (Bundle) trackListActivityIntent.getExtras();
//        spotifyStreamerResult = bundle.getParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult");

        trackListActivityFragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_view_track_search_result, trackListActivityFragment, TRACKLIST_ACTIVITY_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPopulateResult(SpotifyStreamerResult spotifyStreamerResult) {
        this.spotifyStreamerResult = spotifyStreamerResult;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult", spotifyStreamerResult);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
