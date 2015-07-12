package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;

import java.io.IOException;

public class SimplePlayerActivityFragment extends DialogFragment implements MediaPlayer.OnPreparedListener {

    private SpotifyStreamerTrack spotifyStreamerTrack;
    private static final String LOG_TAG = SimplePlayerActivityFragment.class.getSimpleName();
    private MediaPlayer mediaPlayer;


    public SimplePlayerActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotifyStreamerTrack = (SpotifyStreamerTrack) getArguments().getParcelable(getString(R.string.spotify_streamer_track));
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getActivity(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        wifiLock.acquire();
        try {
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setDataSource(spotifyStreamerTrack.getTrackUrl());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
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

        textViewArtistName.setText(spotifyStreamerTrack.getArtistName());
        textViewTrackName.setText(spotifyStreamerTrack.getTrackName());
        textViewAlbumName.setText(spotifyStreamerTrack.getAlbumName());

        return rootView;

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
