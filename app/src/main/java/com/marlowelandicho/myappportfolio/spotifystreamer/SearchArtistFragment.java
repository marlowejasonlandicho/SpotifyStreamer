package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
        inflater.inflate(R.menu.artistfragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        EditText inputArtistNameTextView = (EditText) rootView.findViewById(R.id.input_artist_name);
        inputArtistNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    q = v.getText().toString();
                    if (q != null && q.length() > 0) {
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
//                Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, foreCast);
//                startActivity(detailActivityIntent);

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), selectedArtist.name, duration);
                toast.show();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void updateArtistResult(String q) {
        SearchArtistTask searchArtistTask = new SearchArtistTask();
        searchArtistTask.execute(q);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String location = prefs.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
//        spotifyAPITask.execute(location);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_refresh) {
//            updateArtistResult();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    public class SearchArtistTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        public SearchArtistTask() {
        }

        @Override
        protected void onPostExecute(List<Artist> result) {
            artistAdapter.clear();
            if (result != null && result.isEmpty()) {
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
            if (params.length == 0) {
                return null;
            }
            String artistQuery = params[0];

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotifyService = api.getService();

            ArtistsPager artistsPager = spotifyService.searchArtists(artistQuery);
            List<Artist> artistList = artistsPager.artists.items;
            return artistList;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (q != null && q.length() > 0) {
            updateArtistResult(q);
        }
    }
}
