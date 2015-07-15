package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
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

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;
import com.squareup.picasso.Picasso;

public class SimplePlayerActivityFragment extends DialogFragment {

    private SpotifyStreamerTrack spotifyStreamerTrack;
    private static final String LOG_TAG = SimplePlayerActivityFragment.class.getSimpleName();
    //    private MediaPlayer mediaPlayer;
    private SimplePlayerListener simplePlayerListener;
//    private SpotifyStreamerResult spotifyStreamerResult;

    public interface SimplePlayerListener {
        public void populateResult(SpotifyStreamerTrack spotifyStreamerTrack);

        public void play(SpotifyStreamerTrack spotifyStreamerTrack);

        public void pause(SpotifyStreamerTrack spotifyStreamerTrack);
    }

    public SimplePlayerActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotifyStreamerTrack = (SpotifyStreamerTrack) getArguments().getParcelable(getString(R.string.spotify_streamer_track));
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

        final ImageButton playButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_play);
        final ImageButton pauseButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_pause);
        ImageButton prevButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_prev);
        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.imageButton_player_next);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekbar_player);

        playButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButton.setVisibility(View.GONE);
                        pauseButton.setVisibility(View.VISIBLE);
                        simplePlayerListener.play(spotifyStreamerTrack);
                    }
                }
        );

        pauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pauseButton.setVisibility(View.GONE);
                        playButton.setVisibility(View.VISIBLE);
                        simplePlayerListener.pause(spotifyStreamerTrack);
                    }
                }
        );

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
