package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by marlowe.landicho on 24/6/15.
 */
public class ArtistAdapter extends BaseAdapter {
    Context context;
    List<Artist> artistList;

    public ArtistAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    private class ArtistViewHolder {
        ImageView artistImageView;
        TextView txtViewArtistName;
    }

//    private class TrackViewHolder {
//        ImageView albumImageView;
//        TextView txtViewTrackName;
//        TextView txtViewAlbumName;
//    }

    public View getView(int position, View artistView, ViewGroup parent) {
        ArtistViewHolder artistViewHolder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (artistView == null) {
            artistView = mInflater.inflate(R.layout.list_item_individual_artist_search, null);
            artistViewHolder = new ArtistViewHolder();
            artistViewHolder.artistImageView = (ImageView) artistView.findViewById(R.id.image_view_artist);
            artistViewHolder.txtViewArtistName = (TextView) artistView.findViewById(R.id.text_view_artist_name);
            artistView.setTag(artistViewHolder);
        } else {
            artistViewHolder = (ArtistViewHolder) artistView.getTag();
        }

        Artist artist = (Artist) getItem(position);
        if (artist.images.size() == 3) {
//            artistViewHolder.artistImageView.setImageResource(artist.images.get(2));

        }
        artistViewHolder.txtViewArtistName.setText(artist.name);

        return artistView;
    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    @Override
    public Object getItem(int position) {
        return artistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return artistList.indexOf(getItem(position));
    }
}
