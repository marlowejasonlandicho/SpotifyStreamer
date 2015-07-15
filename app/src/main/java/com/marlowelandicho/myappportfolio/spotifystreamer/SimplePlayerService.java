package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
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

public class SimplePlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.marlowelandicho.myappportfolio.spotifystreamer.play";
    public static final String ACTION_PAUSE = "com.marlowelandicho.myappportfolio.spotifystreamer.pause";
    public static final String ACTION_STOP = "com.marlowelandicho.myappportfolio.spotifystreamer.stop";
    private static final String NOTIFICATION = "com.marlowelandicho.myappportfolio.spotifystreamer.SimplePlayerService";
    private static final int NOTIFICATION_ID = 10;
    private static final String LOG_TAG = SimplePlayerService.class.getSimpleName();
    private SpotifyStreamerTrack spotifyStreamerTrack;
    private MediaPlayer mediaPlayer = null;
    private WifiManager.WifiLock wifiLock;

//    public SimplePlayerService() {
//        super("com.marlowelandicho.myappportfolio.spotifystreamer.SimplePlayerService");
//    }

    public int onStartCommand(Intent intent, int flags, int startId) {
//        mediaPlayer = (MediaPlayer) intent.getExtras().get("media_player");
        spotifyStreamerTrack = intent.getParcelableExtra(getString(R.string.spotify_streamer_track));
//        if (intent.getAction().equals(ACTION_PLAY)) {
        initMediaPlayer();
//        }
        return START_NOT_STICKY;
    }

    private void initMediaPlayer() {
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
    }

    public void onPrepared(MediaPlayer player) {

        this.mediaPlayer = player;
        Intent intent = new Intent(NOTIFICATION);
//        Bundle bundle = new Bundle();
//        intent.putExtra("media_player", this.mediaPlayer);
//        intent.putExtra("result", Activity.RESULT_OK);
//        sendBroadcast(intent);


//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
//
//        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            Toast.makeText(this, "Could not play audio!!!", Toast.LENGTH_LONG).show();
//        } else {
            mediaPlayer.start();
//        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
}