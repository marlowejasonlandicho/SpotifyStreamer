package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;

public class SimplePlayerActivityFragment extends DialogFragment {

    private SpotifyStreamerTrack spotifyStreamerTrack;
    private static final String LOG_TAG = SimplePlayerActivityFragment.class.getSimpleName();
//    private MediaPlayer mediaPlayer;


    public SimplePlayerActivityFragment() {
        // Required empty public constructor
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

        textViewArtistName.setText(spotifyStreamerTrack.getArtistName());
        textViewTrackName.setText(spotifyStreamerTrack.getTrackName());
        textViewAlbumName.setText(spotifyStreamerTrack.getAlbumName());



        return rootView;

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

    public interface PlayerListener {
        public void onPlay(SpotifyStreamerTrack spotifyStreamerTrack);
    }

}
