package com.marlowelandicho.myappportfolio.spotifystreamer.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by marlowe.landicho on 27/6/15.
 */

public class SpotifyStreamerResult {

    private static String queryString;
    private static List<Artist> artists = new ArrayList<>();
    private static Map<String, List<Track>> artistTopTracks = new HashMap<>();


    public static List<Artist> getArtists() {
        return artists;
    }

    public static void setArtists(List<Artist> artists) {
        SpotifyStreamerResult.artists = artists;
    }

    public static void addArtistTopTracks(String artistId, List<Track> tracks) {
        artistTopTracks.put(artistId, tracks);
    }

    public static List<Track> getArtistTopTracks(String artistId) {
        return artistTopTracks.get(artistId);
    }

    public static String getQueryString() {
        return queryString;
    }

    public static void setQueryString(String queryString) {
        SpotifyStreamerResult.queryString = queryString;
    }

    public static void clearSearchArtistResults() {
        artists.clear();
    }
}
