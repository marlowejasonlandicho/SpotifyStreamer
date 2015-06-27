package com.marlowelandicho.myappportfolio.spotifystreamer.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marlowe.landicho on 27/6/15.
 */

public class SpotifyStreamerResult {

    private static String queryString;
    private static int firstVisiblePosition;
    private static List<SpotifyStreamerArtist> artists = new ArrayList<>();
    private static final Map<String, List<SpotifyStreamerTrack>> artistTopTracks = new HashMap<>();


    public static List<SpotifyStreamerArtist> getArtists() {
        return artists;
    }

    public static void setArtists(List<SpotifyStreamerArtist> artists) {
        SpotifyStreamerResult.artists = artists;
    }

    public static void addArtistTopTracks(String artistId, List<SpotifyStreamerTrack> tracks) {
        artistTopTracks.put(artistId, tracks);
    }

    public static List<SpotifyStreamerTrack> getArtistTopTracks(String artistId) {
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

    public static int getFirstVisiblePosition() {
        return firstVisiblePosition;
    }

    public static void setFirstVisiblePosition(int firstVisiblePosition) {
        SpotifyStreamerResult.firstVisiblePosition = firstVisiblePosition;
    }
}
