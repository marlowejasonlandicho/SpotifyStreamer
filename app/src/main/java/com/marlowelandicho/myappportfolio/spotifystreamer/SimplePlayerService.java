package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;

import java.io.IOException;

public class SimplePlayerService extends IntentService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String ACTION_PLAY = "com.marlowelandicho.myappportfolio.spotifystreamer.play";
    private static final int NOTIFICATION_ID = 10;
    private static final String LOG_TAG = SimplePlayerService.class.getSimpleName();
    private SpotifyStreamerTrack spotifyStreamerTrack;

    private MediaPlayer mediaPlayer = null;

    public SimplePlayerService() {
        super("com.marlowelandicho.myappportfolio.spotifystreamer.SimplePlayerService");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

//        intent.get
//        if (intent.getAction().equals(ACTION_PLAY)) {
        spotifyStreamerTrack = intent.getParcelableExtra(getString(R.string.spotify_streamer_track));
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();
        try {
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setDataSource(spotifyStreamerTrack.getPreviewUrl());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return START_STICKY;
//        }
    }

    public void onPrepared(MediaPlayer player) {
        mediaPlayer.start();
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SimplePlayerActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        notification.tickerText = "Now playing: " + spotifyStreamerTrack.getTrackName() + " - " + spotifyStreamerTrack.getArtistName();
        notification.icon = R.mipmap.ic_launcher;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(getApplicationContext(), "SimplePlayer",
                "Playing: " + spotifyStreamerTrack.getTrackName(), pi);
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}