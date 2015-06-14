package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    private String[] weatherResults;
    private ArrayAdapter arrayAdapter;

    public ArtistFragment() {
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
        View rootView = inflater.inflate(R.layout.artist_fragment_main, container, false);
        List forecastList = new ArrayList<String>();
        forecastList.add("");

//        arrayAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.list_item_layout,
//                R.id.list_item_forecast_textview,
//                forecastList);
//
//        ListView listView = (ListView) rootView.findViewById(R.id.list_view_forecast);
//        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String foreCast = (String) parent.getItemAtPosition(position);
//                Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, foreCast);
//                startActivity(detailActivityIntent);
//
//            }
//        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeatherData();
    }


    private void updateWeatherData() {
        SpotifyAPITask spotifyAPITask = new SpotifyAPITask();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String location = prefs.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
//        spotifyAPITask.execute(location);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateWeatherData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SpotifyAPITask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = SpotifyAPITask.class.getSimpleName();
        private String postalCode;

        public SpotifyAPITask() {
        }

        @Override
        protected void onPostExecute(String[] result) {
            arrayAdapter.clear();
            for (String resultStr : result) {
                arrayAdapter.add(resultStr);
            }
        }


        @Override
        protected String[] doInBackground(String... params) {


            try {
                SpotifyApi api = new SpotifyApi();

//                api.setAccessToken("myAccessToken");
                SpotifyService spotify = api.getService();
//                spotify.searchArtists();
//                spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
//                    @Override
//                    public void success(Album album, Response response) {
//                        Log.d("Album success", album.name);
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Log.d("Album failure", error.toString());
//                    }
//                });

            } finally {

            }
            return null;
        }


    }

}
