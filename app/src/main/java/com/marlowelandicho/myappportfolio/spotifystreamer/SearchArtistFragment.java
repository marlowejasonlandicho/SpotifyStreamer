package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        FragmentActivity fragmentActivity = getActivity();
        int listItemLayout = R.layout.list_item_individual_artist_search;
        int listItem = R.id.list_view_artist_search_result;


        ListView listView = (ListView) rootView.findViewById(R.id.list_view_artist_search_result);
        ArtistAdapter adapter = new ArtistAdapter(this, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String foreCast = (String) parent.getItemAtPosition(position);
//                Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, foreCast);
//                startActivity(detailActivityIntent);

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "TEST", duration);
                toast.show();
            }
        });


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
////                int duration = Toast.LENGTH_SHORT;
////                Toast toast = Toast.makeText(getActivity().getApplicationContext(), foreCast, duration);
////                toast.show();
//            }
//        });


//        List forecastList = new ArrayList<String>();
//        forecastList.add("");

        artistAdapter = new ArrayAdapter<Artist>(
                getActivity(),
                R.layout.list_item_layout,
                R.id.list_item_forecast_textview,
                forecastList);
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


        ListView lv = (ListView) rootView.findViewById(R.id.list_view_artist_search_result);

        // create the grid item mapping
        String[] from = new String[]{"id", "name", "url", "height", "width"};
//        int[] to = new int[]{R.id.item1, R.id.item2, R.id.item3, R.id.item4};

        // prepare the list of all records
        List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> artistMap = new HashMap<String, String>();
            artistMap.put("id", "" + i);
            artistMap.put("name", "col_1_item_" + i);
            artistMap.put("url", "url" + i);
            artistMap.put("height", "col_2_item_" + i);
            artistMap.put("width", "col_3_item_" + i);
            resultList.add(artistMap);
        }

        // fill in the grid_item layout
//        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.grid_item, from, to);
//        lv.setAdapter(adapter);


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


    public class SpotifyAPITask extends AsyncTask<String, Void, List<Map<String, String>>> {

        private final String LOG_TAG = SpotifyAPITask.class.getSimpleName();
        private String postalCode;

        public SpotifyAPITask() {
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {

//            arrayAdapter.clear();
//            for (String resultStr : result) {
//                arrayAdapter.add(resultStr);
//            }
        }


        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            List<Map<String, String>> mapArrayList = new ArrayList<Map<String, String>>();
            try {
                if (params.length == 0) {
                    return null;
                }
                String artistQuery = params[0];

                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                ArtistsPager artistsPager = spotify.searchArtists(artistQuery);
                List<Artist> artistList = artistsPager.artists.items;
                for (Artist artist : artistList) {
                    String artistId = artist.id;
                    String artistName = artist.name;
                    List<Image> artistImages = artist.images;
                }

                return mapArrayList;

            } finally {

            }
//            return mapArrayList;

        }

    }


}

}
