package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackListActivityFragment extends Fragment {

    private TopTrackAdapter topTrackAdapter;
    private List<Track> topTrackResultList = new ArrayList<>();
    private static final Map<String, Object> paramMap = new HashMap<>();
    String artistId;

    private static final String LOG_TAG = TrackListActivityFragment.class.getSimpleName();

    public TrackListActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_search, container, false);
        List<Track> localTrackList = null;

        paramMap.put("country", Locale.getDefault().getCountry());
        Intent trackListActivityIntent = getActivity().getIntent();
        if (trackListActivityIntent != null && trackListActivityIntent.hasExtra(Intent.EXTRA_TEXT)) {
            artistId = trackListActivityIntent.getStringExtra(Intent.EXTRA_TEXT);
            localTrackList = SpotifyStreamerResult.getArtistTopTracks(artistId);
            if (localTrackList != null && localTrackList.size() > 0) {
                topTrackResultList.addAll(SpotifyStreamerResult.getArtistTopTracks(artistId));
            }
        }

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_track_search_result);
        topTrackAdapter = new TopTrackAdapter(getActivity().getApplicationContext(), topTrackResultList);
        listView.setAdapter(topTrackAdapter);

        if (localTrackList == null || localTrackList.size() == 0) {
            updateTopTrackResult(artistId);
        }

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void updateTopTrackResult(String artistIdParam) {
        SearchTopTrackTask searchTopTrackTask = new SearchTopTrackTask();
        searchTopTrackTask.execute(artistIdParam);
    }

    public void onPause() {
        super.onPause();
        SpotifyStreamerResult.addArtistTopTracks(artistId, topTrackResultList);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        q = SpotifyStreamerResult.getQueryString();
//        topTrackResultList.addAll(SpotifyStreamerResult.getArtistTopTracks(artistId));
//    }

    public class SearchTopTrackTask extends AsyncTask<String, Void, List<Track>> {

        private final String LOG_TAG = SearchTopTrackTask.class.getSimpleName();

        public SearchTopTrackTask() {
        }

        @Override
        protected void onPostExecute(List<Track> result) {
            topTrackAdapter.clear();
            if (result == null || result.isEmpty()) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.artist_no_result), duration);
                toast.show();
            } else {
                for (Track topTrackResult : result) {
                    topTrackAdapter.add(topTrackResult);
                }
            }
            topTrackAdapter.notifyDataSetChanged();

        }

        @Override
        protected List<Track> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String artistIdParam = params[0];

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotifyService = api.getService();

            Tracks topTracks = spotifyService.getArtistTopTrack(artistIdParam, paramMap);
            List<Track> topTracksList = topTracks.tracks;
            topTrackResultList = topTracksList;

//            List<Track> topTracksList = new ArrayList<Track>();
//            Track track = new Track();
//            track.name = "Track 1";
//            track.album = new AlbumSimple();
//            track.album.name = "Album 1";
//            topTracksList.add(track);
            return topTracksList;
        }
    }

}
