package com.marlowelandicho.myappportfolio.spotifystreamer.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marlowe.landicho on 27/6/15.
 */

public class SpotifyStreamerResult implements Parcelable {

    private String artistId;
    private String queryString;
    private int firstVisiblePosition;
    private List<SpotifyStreamerArtist> artists = new ArrayList<>();
    //    private final List<SpotifyStreamerTrack> artistTopTracks = new ArrayList<>();
    private Map<String, List<SpotifyStreamerTrack>> artistTopTracks = new HashMap<>();

    public static final Parcelable.Creator<SpotifyStreamerResult> CREATOR =
            new Parcelable.Creator<SpotifyStreamerResult>() {
                @Override
                public SpotifyStreamerResult createFromParcel(Parcel source) {
                    return new SpotifyStreamerResult(source);
                }

                @Override
                public SpotifyStreamerResult[] newArray(int size) {
                    return new SpotifyStreamerResult[size];
                }
            };

    public SpotifyStreamerResult() {
    }

    public SpotifyStreamerResult(Parcel source) {
        artistId = source.readString();
        queryString = source.readString();
        firstVisiblePosition = source.readInt();
        source.readList(artists, SpotifyStreamerArtist.class.getClassLoader());
//        source.readList(artistTopTracks, null);
        source.readMap(artistTopTracks, SpotifyStreamerTrack.class.getClassLoader());
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public int getFirstVisiblePosition() {
        return firstVisiblePosition;
    }

    public void setFirstVisiblePosition(int firstVisiblePosition) {
        this.firstVisiblePosition = firstVisiblePosition;
    }

    public List<SpotifyStreamerArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<SpotifyStreamerArtist> artists) {
        this.artists = artists;
    }

    public Map<String, List<SpotifyStreamerTrack>> getArtistTopTracks() {
        return artistTopTracks;
    }

    public void setArtistTopTracks(Map<String, List<SpotifyStreamerTrack>> artistTopTracks) {
        this.artistTopTracks = artistTopTracks;
    }

    public void addArtistTopTracks(String artistId, List<SpotifyStreamerTrack> tracks) {
        artistTopTracks.clear();
        artistTopTracks.put(artistId, tracks);
    }

    public List<SpotifyStreamerTrack> getArtistTopTracks(String artistId) {
        return artistTopTracks.get(artistId);
    }

    public void clearSearchArtistResults() {
        artists.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(queryString);
        dest.writeInt(firstVisiblePosition);
        dest.writeList(artists);
//        dest.writeList(artistTopTracks);
        dest.writeMap(artistTopTracks);
    }

}
