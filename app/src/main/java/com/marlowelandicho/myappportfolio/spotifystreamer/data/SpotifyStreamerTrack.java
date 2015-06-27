package com.marlowelandicho.myappportfolio.spotifystreamer.data;

import java.io.Serializable;

/**
 * Created by marlowe.landicho on 27/6/15.
 */
public class SpotifyStreamerTrack implements Serializable {

    private String artistId;
    private String artistName;
    private String albumName;
    private String thumbnailUrl;

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
