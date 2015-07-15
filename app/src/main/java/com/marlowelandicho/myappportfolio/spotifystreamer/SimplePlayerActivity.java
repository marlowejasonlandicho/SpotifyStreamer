package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;


public class SimplePlayerActivity extends AppCompatActivity implements SimplePlayerActivityFragment.SimplePlayerListener {
    private static final String SIMPLEPLAYER_ACTIVITY_FRAGMENT = "SPTAG";
    private SpotifyStreamerResult spotifyStreamerResult;
    private SpotifyStreamerTrack spotifyStreamerTrack;
    private SimplePlayerActivityFragment simplePlayerActivityFragment;
    //    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Intent mediaPlayerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mediaPlayerIntent = new Intent(this, SimplePlayerService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);

        Intent playTrackActivityIntent = getIntent();
        simplePlayerActivityFragment = new SimplePlayerActivityFragment();
        Bundle bundle = (Bundle) playTrackActivityIntent.getExtras();
        this.spotifyStreamerResult = bundle.getParcelable(getString(R.string.spotify_streamer_result));

        simplePlayerActivityFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.simple_player, simplePlayerActivityFragment, SIMPLEPLAYER_ACTIVITY_FRAGMENT)
                .addToBackStack(null)
                .commit();


//        simplePlayerActivityFragment.show(getSupportFragmentManager(), SIMPLEPLAYER_ACTIVITY_FRAGMENT);
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
        } else if (id == android.R.id.home) {
            populateResult(spotifyStreamerTrack);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void populateResult(SpotifyStreamerTrack spotifyStreamerTrack) {
        this.spotifyStreamerTrack = spotifyStreamerTrack;
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.spotify_streamer_result), this.spotifyStreamerResult);
        bundle.putParcelable(getString(R.string.spotify_streamer_track), this.spotifyStreamerTrack);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    @Override
    public void play(SpotifyStreamerTrack spotifyStreamerTrack) {
        mediaPlayerIntent.setAction(SimplePlayerService.ACTION_PLAY);
        mediaPlayerIntent.putExtra(getString(R.string.spotify_streamer_track), spotifyStreamerTrack);

        // add infos for the service which file to download and where to store
        startService(mediaPlayerIntent);
    }

    @Override
    public void pause(SpotifyStreamerTrack spotifyStreamerTrack) {
        stopService(mediaPlayerIntent);
    }
}
