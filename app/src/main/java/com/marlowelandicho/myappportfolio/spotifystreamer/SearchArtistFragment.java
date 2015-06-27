package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerResult;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends Fragment {

    private ArtistAdapter artistAdapter;
    private List<Artist> searchArtistResultList = new ArrayList<>();
    private String q;

    public SearchArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        EditText inputArtistNameTextView = (EditText) rootView.findViewById(R.id.input_artist_name);
        String localQ = SpotifyStreamerResult.getQueryString();
        if (localQ != null) {
            inputArtistNameTextView.setText(localQ);
        }
        inputArtistNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    q = v.getText().toString();
                    if (q != null && q.length() > 0) {
                        SpotifyStreamerResult.clearSearchArtistResults();
                        updateArtistResult(q);
                        return false;
                    }
                }
                return false;
            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_artist_search_result);
        artistAdapter = new ArtistAdapter(getActivity().getApplicationContext(), searchArtistResultList);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist selectedArtist = (Artist) parent.getItemAtPosition(position);
                Intent trackListActivityIntent = new Intent(getActivity(), TrackListActivity.class).putExtra(Intent.EXTRA_TEXT, selectedArtist.id);
                startActivity(trackListActivityIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateArtistResult(String q) {
        SearchArtistTask searchArtistTask = new SearchArtistTask();
        searchArtistTask.execute(q);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        SpotifyStreamerResult.setQueryString(q);
        SpotifyStreamerResult.getArtists().addAll(searchArtistResultList);
    }

    @Override
    public void onResume() {
        super.onResume();
        q = SpotifyStreamerResult.getQueryString();
        searchArtistResultList.addAll(SpotifyStreamerResult.getArtists());
    }


    public class SearchArtistTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        public SearchArtistTask() {
        }

        @Override
        protected void onPostExecute(List<Artist> result) {
            artistAdapter.clear();
            if (result == null || result.isEmpty()) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.artist_no_result), duration);
                toast.show();
            } else {
                for (Artist artistResult : result) {
                    artistAdapter.add(artistResult);
                }
            }
            artistAdapter.notifyDataSetChanged();

        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            if (params == null) {
                return searchArtistResultList;
            }
            String artistQuery = params[0];

            if (artistQuery == null || artistQuery.length() == 0) {
                return searchArtistResultList;
            }
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotifyService = api.getService();

            ArtistsPager artistsPager = spotifyService.searchArtists(artistQuery);
            List<Artist> artistList = artistsPager.artists.items;
            searchArtistResultList = artistList;

//            List<Artist> artistList = new ArrayList<>();
//            Artist artist = new Artist();
//            artist.name = "Artist 1";
//            artistList.add(artist);

            return artistList;
        }
    }


}
