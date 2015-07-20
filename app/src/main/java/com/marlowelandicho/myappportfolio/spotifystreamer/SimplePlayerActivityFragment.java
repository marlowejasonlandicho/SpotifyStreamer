package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimplePlayerActivityFragment extends DialogFragment {

    private SpotifyStreamerTrack spotifyStreamerTrack;
    private static final String LOG_TAG = SimplePlayerActivityFragment.class.getSimpleName();
    private List<SpotifyStreamerTrack> trackList;
    //    private MediaPlayer mediaPlayer;
    private SimplePlayerListener simplePlayerListener;
    private ImageButton playImageButton;
    private ImageButton pauseImageButton;
    private ImageButton prevImageButton;
    private ImageButton nextImageButton;
    private SeekBar seekBar;

    private Intent mediaPlayerIntent;
    private SpotifyStreamerResult spotifyStreamerResult;

    public interface SimplePlayerListener {
        public void populateResult(SpotifyStreamerTrack spotifyStreamerTrack);

        public void play(SpotifyStreamerTrack spotifyStreamerTrack);

        public void initPlayer(SpotifyStreamerTrack spotifyStreamerTrack);

        public void pause(SpotifyStreamerTrack spotifyStreamerTrack);

        public void next(SpotifyStreamerTrack spotifyStreamerTrack);

        public void previous(SpotifyStreamerTrack spotifyStreamerTrack);

        public MediaPlayer getMediaPlayerInstance();
    }

    public SimplePlayerActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotifyStreamerResult = getArguments().getParcelable(getString(R.string.spotify_streamer_result));
//            trackList = spotifyStreamerResult.getArtistTopTracks();
            spotifyStreamerTrack = getArguments().getParcelable(getString(R.string.spotify_streamer_track));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_simple_player, container, false);

        TextView textViewArtistName = (TextView) rootView.findViewById(R.id.player_text_view_artist_name);
        TextView textViewTrackName = (TextView) rootView.findViewById(R.id.player_text_view_track_name);
        TextView textViewAlbumName = (TextView) rootView.findViewById(R.id.player_text_view_album_name);
        ImageView albumImageView = (ImageView) rootView.findViewById(R.id.player_image_view_album);
        try {
            if (spotifyStreamerTrack.getThumbnailUrl() != null) {
                Picasso.with(getActivity())
                        .load(spotifyStreamerTrack.getThumbnailUrl())
                        .resize(200, 200)
                        .centerCrop()
                        .into(albumImageView);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        textViewArtistName.setText(spotifyStreamerTrack.getArtistName());
        textViewTrackName.setText(spotifyStreamerTrack.getTrackName());
        textViewAlbumName.setText(spotifyStreamerTrack.getAlbumName());

        playImageButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_play);
        
        pauseImageButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_pause);
        prevImageButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_prev);
        nextImageButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_next);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekbar_player);

        playImageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playImageButton.setVisibility(View.GONE);
                        pauseImageButton.setVisibility(View.VISIBLE);
                        simplePlayerListener.play(spotifyStreamerTrack);
                    }
                }
        );

        pauseImageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pauseImageButton.setVisibility(View.GONE);
                        playImageButton.setVisibility(View.VISIBLE);
                        simplePlayerListener.pause(spotifyStreamerTrack);
                    }
                }
        );

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simplePlayerListener.next(spotifyStreamerTrack);
            }
        });

        prevImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simplePlayerListener.previous(spotifyStreamerTrack);

            }
        });

//        simplePlayerListener.initPlayer(spotifyStreamerTrack);

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            simplePlayerListener = (SimplePlayerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SimplePlayerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        simplePlayerListener = null;
    }
/*

    public static class SimplePlayerService extends Service implements
            MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

        public static final String ACTION_PLAY = "com.marlowelandicho.myappportfolio.spotifystreamer.play";
        public static final String ACTION_PAUSE = "com.marlowelandicho.myappportfolio.spotifystreamer.pause";
        public static final String ACTION_STOP = "com.marlowelandicho.myappportfolio.spotifystreamer.stop";
        private static final String NOTIFICATION = "com.marlowelandicho.myappportfolio.spotifystreamer.SimplePlayerService";
        private static final int NOTIFICATION_ID = 10;
        private final String LOG_TAG = SimplePlayerService.class.getSimpleName();
        private SpotifyStreamerTrack spotifyStreamerTrack;
        private WifiManager.WifiLock wifiLock;


//    public SimplePlayerService() {
//        super("com.marlowelandicho.myappportfolio.spotifystreamer.SimplePlayerService");
//    }

        public int onStartCommand(Intent intent, int flags, int startId) {
            spotifyStreamerTrack = intent.getParcelableExtra(getString(R.string.spotify_streamer_track));
//        if (intent.getAction().equals(ACTION_PLAY)) {
            initMediaPlayer();
//        }
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
                mediaPlayer.setDataSource(spotifyStreamerTrack.getPreviewUrl());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(SimplePlayerService.this);
                mediaPlayer.setOnErrorListener(SimplePlayerService.this);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }

        public void onPrepared(MediaPlayer player) {
            mediaPlayer = player;
            playImageButton.setEnabled(true);
//            pauseImageButton.setEnabled(true);
            prevImageButton.setEnabled(true);
            nextImageButton.setEnabled(true);
            playImageButton.setVisibility(View.GONE);
            pauseImageButton.setVisibility(View.VISIBLE);

//            Intent intent = new Intent(NOTIFICATION);
//        Bundle bundle = new Bundle();
//        intent.putExtra("media_player", this.mediaPlayer);
//        intent.putExtra("result", Activity.RESULT_OK);
//        sendBroadcast(intent);

            player.start();

//            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    if (fromUser) {
//                        mediaPlayer.seekTo(progress);
//                    }
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                }
//            });
//
//            Thread seekBarThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    int currentPosition = 0;
//                    int total = mediaPlayer.getDuration();
//                    seekBar.setMax(total);
//                    while (mediaPlayer != null &&
//                            currentPosition < total) {
//                        try {
//                            Thread.sleep(1000);
//                            currentPosition = mediaPlayer.getCurrentPosition();
//                        } catch (InterruptedException e) {
//                            return;
//                        } catch (Exception e) {
//                            return;
//                        }
//                        seekBar.setProgress(currentPosition);
//                    }
//                }
//            });

//            createNotification();

//            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
//                    AudioManager.AUDIOFOCUS_GAIN);
//            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                Toast.makeText(this, "Could not play audio!!!", Toast.LENGTH_LONG).show();
//            }

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
    */
}