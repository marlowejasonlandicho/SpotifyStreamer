package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;

import java.util.List;


public class SimplePlayerActivity extends AppCompatActivity implements SimplePlayerActivityFragment.SimplePlayerListener {
    private static final String SIMPLEPLAYER_ACTIVITY_FRAGMENT = "SPTAG";

    private SimplePlayerService simplePlayerService;
    boolean serviceBound = false;

    private SpotifyStreamerResult spotifyStreamerResult;
    private SpotifyStreamerTrack spotifyStreamerTrack;
    private List<SpotifyStreamerTrack> trackList;
    private SimplePlayerActivityFragment simplePlayerActivityFragment;
    private ImageButton playImageButton;
    private ImageButton pauseImageButton;
    private ImageButton prevImageButton;
    private ImageButton nextImageButton;
    private SeekBar seekBar;


    private ServiceConnection simplePlayerServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SimplePlayerService.SimplePlayerServiceBinder simplePlayerServiceBinder = (SimplePlayerService.SimplePlayerServiceBinder) service;
            simplePlayerService = simplePlayerServiceBinder.getService();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.simple_player, simplePlayerActivityFragment, SIMPLEPLAYER_ACTIVITY_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
//        simplePlayerActivityFragment.show(getSupportFragmentManager(), SIMPLEPLAYER_ACTIVITY_FRAGMENT);

            
//            playImageButton = (ImageButton) findViewById(R.id.imageButton_player_play);
//            pauseImageButton = (ImageButton) findViewById(R.id.imageButton_player_pause);
//            prevImageButton = (ImageButton) findViewById(R.id.imageButton_player_prev);
//            nextImageButton = (ImageButton) findViewById(R.id.imageButton_player_next);
//
//            playImageButton.setEnabled(true);
//            prevImageButton.setEnabled(true);
//            nextImageButton.setEnabled(true);


            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);

        Intent playTrackActivityIntent = getIntent();
        simplePlayerActivityFragment = new SimplePlayerActivityFragment();
        Bundle bundle = (Bundle) playTrackActivityIntent.getExtras();

        this.spotifyStreamerResult = bundle.getParcelable(getString(R.string.spotify_streamer_result));
//        this.trackList = this.spotifyStreamerResult.getArtistTopTracks();
        this.spotifyStreamerTrack = bundle.getParcelable(getString(R.string.spotify_streamer_track));

        simplePlayerActivityFragment.setArguments(bundle);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent mediaPlayerIntent = new Intent(this, SimplePlayerService.class);
        mediaPlayerIntent.putExtra(getString(R.string.spotify_streamer_track), spotifyStreamerTrack);
        mediaPlayerIntent.putExtra(getString(R.string.spotify_streamer_result), spotifyStreamerResult);

        bindService(mediaPlayerIntent, simplePlayerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceBound) {
            unbindService(simplePlayerServiceConnection);
            serviceBound = false;
        }
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
        simplePlayerService.play();
    }

    @Override
    public void initPlayer(SpotifyStreamerTrack spotifyStreamerTrack) {
        this.spotifyStreamerTrack = spotifyStreamerTrack;
    }

    @Override
    public void pause(SpotifyStreamerTrack spotifyStreamerTrack) {
        simplePlayerService.pause();
    }

    @Override
    public void next(SpotifyStreamerTrack spotifyStreamerTrack) {
        simplePlayerService.next(spotifyStreamerTrack);
    }

    @Override
    public void previous(SpotifyStreamerTrack spotifyStreamerTrack) {
        simplePlayerService.previous(spotifyStreamerTrack);
    }

    @Override
    public MediaPlayer getMediaPlayerInstance() {
        return simplePlayerService.getMediaPlayerInstance();
    }

}
