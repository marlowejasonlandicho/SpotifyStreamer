package com.marlowelandicho.myappportfolio.spotifystreamer.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marlowe.landicho on 27/6/15.
 */
public class SpotifyStreamerArtist implements Parcelable {

    private String artistId;
    private String artistName;
    private String thumbnailUrl;
    private String trackUrl;

    public static final Parcelable.Creator<SpotifyStreamerArtist> CREATOR =
            new Parcelable.Creator<SpotifyStreamerArtist>() {
                @Override
                public SpotifyStreamerArtist createFromParcel(Parcel source) {
                    return new SpotifyStreamerArtist(source);
                }

                @Override
                public SpotifyStreamerArtist[] newArray(int size) {
                    return new SpotifyStreamerArtist[size];
                }
            };

    public SpotifyStreamerArtist() {
    }

    public SpotifyStreamerArtist(Parcel source) {
        artistId = source.readString();
        artistName = source.readString();
        thumbnailUrl = source.readString();
        trackUrl = source.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(thumbnailUrl);
        dest.writeString(trackUrl);
    }
}
