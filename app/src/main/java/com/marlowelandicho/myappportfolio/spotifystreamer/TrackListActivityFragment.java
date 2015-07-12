package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackListActivityFragment extends Fragment {

    private TopTrackAdapter topTrackAdapter;
    private List<SpotifyStreamerTrack> topTrackResultList = new ArrayList<>();
    private static final Map<String, Object> paramMap = new HashMap<>();
    private String artistId;
    private SpotifyStreamerResult spotifyStreamerResult;
    private static final String LOG_TAG = TrackListActivityFragment.class.getSimpleName();
    private ResultListener resultListener;

    public interface ResultListener {
        public void populateResult(SpotifyStreamerResult spotifyStreamerResult);
    }

    public TrackListActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_search, container, false);
        List<SpotifyStreamerTrack> localTrackList = null;

        paramMap.put("country", Locale.getDefault().getCountry());
        spotifyStreamerResult = (SpotifyStreamerResult) getArguments().getParcelable("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult");

        if (spotifyStreamerResult != null) {
            artistId = spotifyStreamerResult.getArtistId();
            localTrackList = spotifyStreamerResult.getArtistTopTracks(artistId);
            if (localTrackList != null && localTrackList.size() > 0) {
                topTrackResultList.addAll(spotifyStreamerResult.getArtistTopTracks(artistId));
            }
        }

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_track_search_result);
        topTrackAdapter = new TopTrackAdapter(getActivity(), topTrackResultList);
        listView.setAdapter(topTrackAdapter);

        if (localTrackList == null || localTrackList.size() == 0) {
            updateTopTrackResult(artistId);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            resultListener.populateResult(spotifyStreamerResult);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTopTrackResult(String artistIdParam) {
        SearchTopTrackTask searchTopTrackTask = new SearchTopTrackTask();
        searchTopTrackTask.execute(artistIdParam);

//        for (int i = 0; i <= 10; i++) {
//            SpotifyStreamerTrack spotifyStreamerTrack = new SpotifyStreamerTrack();
//            spotifyStreamerTrack.setArtistId("1" + i);
//            spotifyStreamerTrack.setArtistName("A" + i);
//            spotifyStreamerTrack.setAlbumName("B" + i);
//            topTrackResultList.add(spotifyStreamerTrack);
//        }
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            resultListener = (ResultListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ResultListener");
        }
    }

    public class SearchTopTrackTask extends AsyncTask<String, Void, List<SpotifyStreamerTrack>> {

        private final String LOG_TAG = SearchTopTrackTask.class.getSimpleName();

        public SearchTopTrackTask() {
        }

        @Override
        protected void onPostExecute(List<SpotifyStreamerTrack> result) {
            topTrackAdapter.clear();
            if (result == null || result.isEmpty()) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.track_no_result), duration);
                toast.show();
            } else {
                for (SpotifyStreamerTrack topTrackResult : result) {
                    topTrackAdapter.add(topTrackResult);
                }
            }
            spotifyStreamerResult.addArtistTopTracks(artistId, topTrackResultList);
            topTrackAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<SpotifyStreamerTrack> doInBackground(String... params) {
            List<SpotifyStreamerTrack> spotifyStreamerTrackList = new ArrayList<>();
            if (params.length == 0) {
                return null;
            }
            String artistIdParam = params[0];
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotifyService = api.getService();

                Tracks topTracks = spotifyService.getArtistTopTrack(artistIdParam, paramMap);
                List<Track> topTracksList = topTracks.tracks;


                for (Track track : topTracksList) {
                    SpotifyStreamerTrack spotifyStreamerTrack = new SpotifyStreamerTrack();
                    spotifyStreamerTrack.setArtistId(track.id);
                    spotifyStreamerTrack.setArtistName(track.name);
                    spotifyStreamerTrack.setAlbumName(track.album.name);
                    for (Image image : track.album.images) {
                        if (image.height <= 200 && image.url != null) {
                            spotifyStreamerTrack.setThumbnailUrl(image.url);
                            break;
                        }
                    }
                    spotifyStreamerTrackList.add(spotifyStreamerTrack);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            topTrackResultList = spotifyStreamerTrackList;
            return spotifyStreamerTrackList;
        }
    }

}
