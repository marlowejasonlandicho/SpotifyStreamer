package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerArtist;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by marlowe.landicho on 24/6/15.
 */
public class ArtistAdapter extends BaseAdapter {

    private final String LOG_TAG = ArtistAdapter.class.getSimpleName();
    private final Context context;
    private final List<SpotifyStreamerArtist> artistList;

    public ArtistAdapter(Context context, List<SpotifyStreamerArtist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    private class ArtistViewHolder {
        ImageView artistImageView;
        TextView txtViewArtistName;
    }

    public View getView(int position, View artistView, ViewGroup parent) {
        ArtistViewHolder artistViewHolder;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        SpotifyStreamerArtist artist = (SpotifyStreamerArtist) getItem(position);

        if (artistView == null) {
            artistView = mInflater.inflate(R.layout.list_item_individual_artist_search, parent, false);
            artistViewHolder = new ArtistViewHolder();
            artistViewHolder.artistImageView = (ImageView) artistView.findViewById(R.id.image_view_artist);
            artistViewHolder.txtViewArtistName = (TextView) artistView.findViewById(R.id.text_view_artist_name);
            artistView.setTag(artistViewHolder);
        } else {
            artistViewHolder = (ArtistViewHolder) artistView.getTag();
        }

        if (artist.getThumbnailUrl() != null) {
            Picasso.with(context)
                    .load(artist.getThumbnailUrl())
                    .resize(50, 50)
                    .centerCrop()
                    .into(artistViewHolder.artistImageView);

        }
        artistViewHolder.txtViewArtistName.setText(artist.getArtistName());

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

    public void add(SpotifyStreamerArtist artist) {
        artistList.add(artist);
    }
}
