package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by marlowe.landicho on 24/6/15.
 */
public class ArtistAdapter extends BaseAdapter {

    private final String LOG_TAG = ArtistAdapter.class.getSimpleName();


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

    public View getView(int position, View artistView, ViewGroup parent) {
        ArtistViewHolder artistViewHolder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Artist artist = (Artist) getItem(position);

        if (artistView == null) {
            artistView = mInflater.inflate(R.layout.list_item_individual_artist_search, parent, false);
            artistViewHolder = new ArtistViewHolder();
            artistViewHolder.artistImageView = (ImageView) artistView.findViewById(R.id.image_view_artist);
            artistViewHolder.txtViewArtistName = (TextView) artistView.findViewById(R.id.text_view_artist_name);
            artistView.setTag(artistViewHolder);
        } else {
            artistViewHolder = (ArtistViewHolder) artistView.getTag();
        }

        for (Image image : artist.images) {
            if (image.height <= 64 && image.url != null) {
                Picasso.with(context)
                        .load(image.url)
                        .resize(50, 50)
                        .centerCrop()
                        .into(artistViewHolder.artistImageView);
                break;
            }
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


    public void clear() {
        artistList.clear();
    }

    public void add(Artist artist) {
        artistList.add(artist);
    }
}
