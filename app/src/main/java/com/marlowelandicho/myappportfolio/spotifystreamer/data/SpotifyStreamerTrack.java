package com.marlowelandicho.myappportfolio.spotifystreamer.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marlowe.landicho on 27/6/15.
 */
public class SpotifyStreamerTrack implements Parcelable {

    private String artistId;
    private String artistName;
    private String trackName;
    private String albumName;
    private String thumbnailUrl;
    private String trackUrl;
    private String previewUrl;
    private Integer position;

    public static final Parcelable.Creator<SpotifyStreamerTrack> CREATOR =
            new Parcelable.Creator<SpotifyStreamerTrack>() {
                @Override
                public SpotifyStreamerTrack createFromParcel(Parcel source) {
                    return new SpotifyStreamerTrack(source);
                }

                @Override
                public SpotifyStreamerTrack[] newArray(int size) {
                    return new SpotifyStreamerTrack[size];
                }
            };

    public SpotifyStreamerTrack() {
    }

    public SpotifyStreamerTrack(Parcel source) {
        artistId = source.readString();
        artistName = source.readString();
        trackName = source.readString();
        albumName = source.readString();
        thumbnailUrl = source.readString();
        trackUrl = source.readString();
        previewUrl = source.readString();
        position = source.readInt();
    }

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

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
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

    public String getTrackUrl() {
        return trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(trackName);
        dest.writeString(albumName);
        dest.writeString(thumbnailUrl);
        dest.writeString(trackUrl);
        dest.writeString(previewUrl);
        dest.writeInt(position);
    }

}
