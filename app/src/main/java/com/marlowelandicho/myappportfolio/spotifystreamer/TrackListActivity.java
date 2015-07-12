package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;


public class TrackListActivity extends AppCompatActivity implements TrackListActivityFragment.TrackListListener {

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
        trackListActivityFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_view_track_search_result, trackListActivityFragment, TRACKLIST_ACTIVITY_FRAGMENT)
                .addToBackStack(null)
                .commit();
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
    public void populateResult(SpotifyStreamerResult spotifyStreamerResult) {
        this.spotifyStreamerResult = spotifyStreamerResult;
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.spotify_streamer_result), spotifyStreamerResult);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void playTrack(SpotifyStreamerTrack spotifyStreamerTrack) {

        Intent playTrackActivityIntent =
                new Intent(this, SimplePlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.spotify_streamer_track), spotifyStreamerTrack);
        playTrackActivityIntent.putExtras(playTrackActivityIntent);
        startActivity(playTrackActivityIntent);
    }
}
