package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
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
import android.widget.Toast;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimplePlayerActivityFragment extends DialogFragment {

    private SpotifyStreamerTrack spotifyStreamerTrack;
    private static final String LOG_TAG = SimplePlayerActivityFragment.class.getSimpleName();
    private List<SpotifyStreamerTrack> trackList;
    private SimplePlayerListener simplePlayerListener;
    private TextView textViewArtistName;
    private TextView textViewTrackName;
    private TextView textViewAlbumName;
    private ImageView albumImageView;
    private ImageButton playImageButton;
    private ImageButton pauseImageButton;
    private ImageButton prevImageButton;
    private ImageButton nextImageButton;
    private SeekBar seekBar;
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
            trackList = spotifyStreamerResult.getArtistTopTracks();
            spotifyStreamerTrack = getArguments().getParcelable(getString(R.string.spotify_streamer_track));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_simple_player, container, false);

        textViewArtistName = (TextView) rootView.findViewById(R.id.player_text_view_artist_name);
        textViewTrackName = (TextView) rootView.findViewById(R.id.player_text_view_track_name);
        textViewAlbumName = (TextView) rootView.findViewById(R.id.player_text_view_album_name);
        albumImageView = (ImageView) rootView.findViewById(R.id.player_image_view_album);
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
                try {
                    SpotifyStreamerTrack nextSpotifyStreamerTrack = trackList.get(spotifyStreamerTrack.getPosition() + 1);
                    spotifyStreamerTrack = nextSpotifyStreamerTrack;
                    simplePlayerListener.next(spotifyStreamerTrack);
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
                    playImageButton.setVisibility(View.GONE);
                    pauseImageButton.setVisibility(View.VISIBLE);
                } catch (IndexOutOfBoundsException ix) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.artist_no_more_track), duration);
                    toast.show();
                }
            }
        });

        prevImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SpotifyStreamerTrack nextSpotifyStreamerTrack = trackList.get(spotifyStreamerTrack.getPosition() - 1);
                    spotifyStreamerTrack = nextSpotifyStreamerTrack;
                    simplePlayerListener.previous(spotifyStreamerTrack);
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
                    playImageButton.setVisibility(View.GONE);
                    pauseImageButton.setVisibility(View.VISIBLE);

                } catch (IndexOutOfBoundsException ix) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.artist_no_more_track), duration);
                    toast.show();
                }
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

}