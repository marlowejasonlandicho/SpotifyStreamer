package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerArtist;
import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends Fragment {

    private ArtistAdapter artistAdapter;
    private List<SpotifyStreamerArtist> searchArtistResultList = new ArrayList<>();
    private SpotifyStreamerResult spotifyStreamerResult;
    private String q;

    OnPopulateResultListener mCallback;

    public SearchArtistFragment() {
    }

    public interface OnPopulateResultListener {
        public void onPopulateResult(SpotifyStreamerResult spotifyStreamerResult);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spotifyStreamerResult = (SpotifyStreamerResult) getArguments()
                .get("com.marlowelandicho.myappportfolio.spotifystreamer.SpotifyStreamerResult");

        if (savedInstanceState != null && spotifyStreamerResult == null) {

        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_artist_search_result);
        EditText inputArtistNameTextView = (EditText) rootView.findViewById(R.id.input_artist_name);
        String localQ = spotifyStreamerResult.getQueryString();

        if (localQ != null) {
            inputArtistNameTextView.setText(localQ);
        } else {
            inputArtistNameTextView.requestFocus();
        }

        inputArtistNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ListView listView = (ListView) getActivity().findViewById(R.id.list_view_artist_search_result);
                    listView.setSelection(0);

                    q = v.getText().toString();
                    if (q != null && q.length() > 0) {
                        spotifyStreamerResult.clearSearchArtistResults();
                        updateArtistResult(q);
                        return false;
                    }
                }
                return false;
            }
        });

        artistAdapter = new ArtistAdapter(getActivity().getApplicationContext(), searchArtistResultList);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpotifyStreamerArtist selectedArtist = (SpotifyStreamerArtist) parent.getItemAtPosition(position);

                spotifyStreamerResult.setArtistId(selectedArtist.getArtistId());
                spotifyStreamerResult.setQueryString(q);
                spotifyStreamerResult.clearSearchArtistResults();
                spotifyStreamerResult.getArtists().addAll(searchArtistResultList);

                mCallback.onPopulateResult(spotifyStreamerResult);


            }
        });

        return rootView;
    }

    public void updateArtistResult(String q) {
        SearchArtistTask searchArtistTask = new SearchArtistTask();
        searchArtistTask.execute(q);
//        for (int i = 0; i <= 10; i++) {
//            SpotifyStreamerArtist spotifyStreamerArtist = new SpotifyStreamerArtist();
//            spotifyStreamerArtist.setArtistId("A" + i);
//            spotifyStreamerArtist.setArtistName("B" + i);
//            searchArtistResultList.add(spotifyStreamerArtist);
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        ListView listView = (ListView) getActivity().findViewById(R.id.list_view_artist_search_result);
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        spotifyStreamerResult.setFirstVisiblePosition(new Integer(firstVisiblePosition));
//        spotifyStreamerResult.setQueryString(q);
        spotifyStreamerResult.getArtists().clear();
        spotifyStreamerResult.getArtists().addAll(searchArtistResultList);
    }

    @Override
    public void onResume() {
        super.onResume();
        q = spotifyStreamerResult.getQueryString();
        ListView listView = (ListView) getActivity().findViewById(R.id.list_view_artist_search_result);
        listView.setSelection(spotifyStreamerResult.getFirstVisiblePosition());
        listView.requestFocus();

        searchArtistResultList.clear();

        searchArtistResultList.addAll(spotifyStreamerResult.getArtists());
        artistAdapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnPopulateResultListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPopulateResultListener");
        }
    }

    public class SearchArtistTask extends AsyncTask<String, Void, List<SpotifyStreamerArtist>> {

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        public SearchArtistTask() {
        }

        @Override
        protected void onPostExecute(List<SpotifyStreamerArtist> result) {
            artistAdapter.clear();
            if (result == null || result.isEmpty()) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.artist_no_result), duration);
                toast.show();
            } else {
                for (SpotifyStreamerArtist spotifyStreamerArtist : result) {
                    artistAdapter.add(spotifyStreamerArtist);
                }
            }
            artistAdapter.notifyDataSetChanged();

        }

        @Override
        protected List<SpotifyStreamerArtist> doInBackground(String... params) {
            List<SpotifyStreamerArtist> spotifyStreamerArtistList = new ArrayList<>();
            if (params == null) {
                return searchArtistResultList;
            }
            String artistQuery = params[0];

            if (artistQuery == null || artistQuery.length() == 0) {
                return searchArtistResultList;
            }
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotifyService = api.getService();

                ArtistsPager artistsPager = spotifyService.searchArtists(artistQuery);
                List<Artist> artistList = artistsPager.artists.items;
                for (Artist artist : artistList) {
                    SpotifyStreamerArtist spotifyStreamerArtist = new SpotifyStreamerArtist();
                    spotifyStreamerArtist.setArtistId(artist.id);
                    spotifyStreamerArtist.setArtistName(artist.name);
                    for (Image image : artist.images) {
                        if (image.height <= 200 && image.url != null) {
                            spotifyStreamerArtist.setThumbnailUrl(image.url);
                            break;
                        }
                    }
                    spotifyStreamerArtistList.add(spotifyStreamerArtist);
                }

            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            searchArtistResultList = spotifyStreamerArtistList;
            return spotifyStreamerArtistList;
        }
    }


}
