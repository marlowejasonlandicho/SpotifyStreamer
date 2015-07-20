package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;

import java.io.IOException;

public class SimplePlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.marlowelandicho.myappportfolio.spotifystreamer.play";
    public static final String ACTION_PAUSE = "com.marlowelandicho.myappportfolio.spotifystreamer.pause";
    public static final String ACTION_STOP = "com.marlowelandicho.myappportfolio.spotifystreamer.stop";
    private static final String NOTIFICATION = "com.marlowelandicho.myappportfolio.spotifystreamer.SimplePlayerService";
    private static final int NOTIFICATION_ID = 10;
    private final IBinder simplePlayerServiceBinder = new SimplePlayerServiceBinder();
    private NotificationManager notificationManager;
    private MediaPlayer mediaPlayer;
    private final String LOG_TAG = SimplePlayerService.class.getSimpleName();
    private SpotifyStreamerResult spotifyStreamerResult;
    private SpotifyStreamerTrack spotifyStreamerTrack;
    private WifiManager.WifiLock wifiLock;

    public class SimplePlayerServiceBinder extends Binder {
        public SimplePlayerService getService() {
            return SimplePlayerService.this;
        }
    }

//    @Override
//    public void onCreate() {
//
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        spotifyStreamerTrack = intent.getParcelableExtra(getString(R.string.spotify_streamer_track));
//        initMediaPlayer();
        return START_STICKY;
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
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
    }

    public void onPrepared(MediaPlayer player) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mediaPlayer = player;
        mediaPlayer.start();


        createNotification();
//            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
//                    AudioManager.AUDIOFOCUS_GAIN);
//            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                Toast.makeText(this, "Could not play audio!!!", Toast.LENGTH_LONG).show();
//            }
    }

    @Override
    public IBinder onBind(Intent intent) {
        spotifyStreamerTrack = intent.getParcelableExtra(getString(R.string.spotify_streamer_track));
        spotifyStreamerResult = intent.getParcelableExtra(getString(R.string.spotify_streamer_result));

        initMediaPlayer();
        return simplePlayerServiceBinder;
    }

    public MediaPlayer getMediaPlayerInstance() {
        return mediaPlayer;
    }

    public void play() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void previous(SpotifyStreamerTrack prevSpotifyStreamerTrack) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        wifiLock.release();
        spotifyStreamerTrack = prevSpotifyStreamerTrack;
        initMediaPlayer();
    }

    public void next(SpotifyStreamerTrack nextSpotifyStreamerTrack) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        wifiLock.release();
        spotifyStreamerTrack = nextSpotifyStreamerTrack;
        initMediaPlayer();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(NOTIFICATION_ID);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        wifiLock.release();
        stopForeground(true);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) {
                    initMediaPlayer();
                } else if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    createNotification();
                }
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0.1f, 0.1f);
                }
                break;
        }
    }

    private void createNotification() {


        Intent notificationIntent = new Intent(this, SimplePlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.spotify_streamer_result), this.spotifyStreamerResult);
        bundle.putParcelable(getString(R.string.spotify_streamer_track), this.spotifyStreamerTrack);

        notificationIntent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new Notification();
//        notification.contentIntent = pi;
        notification.tickerText = "Now playing: " + spotifyStreamerTrack.getTrackName() + " - " + spotifyStreamerTrack.getArtistName();
        notification.icon = R.mipmap.ic_launcher;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(this, "SimplePlayer",
                "Playing: " + spotifyStreamerTrack.getTrackName(), pi);
//        notificationManager.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);
    }

}