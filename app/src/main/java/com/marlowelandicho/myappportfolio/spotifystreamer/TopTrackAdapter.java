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

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by marlowe.landicho on 24/6/15.
 */
public class TopTrackAdapter extends BaseAdapter {

    private final String LOG_TAG = TopTrackAdapter.class.getSimpleName();


    Context context;
    List<Track> trackList;

    public TopTrackAdapter(Context context, List<Track> trackList) {
        this.context = context;
        this.trackList = trackList;
    }


    private class TrackViewHolder {
        ImageView albumImageView;
        TextView txtViewTrackName;
        TextView txtViewAlbumName;
    }

    public View getView(int position, View topTrackView, ViewGroup parent) {
        TrackViewHolder trackViewHolder;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Track track = (Track) getItem(position);

        if (topTrackView == null) {
            topTrackView = mInflater.inflate(R.layout.list_item_individual_track_search, parent, false);
            trackViewHolder = new TrackViewHolder();
            trackViewHolder.albumImageView = (ImageView) topTrackView.findViewById(R.id.image_view_track);
            trackViewHolder.txtViewTrackName = (TextView) topTrackView.findViewById(R.id.text_view_track_name);
            trackViewHolder.txtViewAlbumName = (TextView) topTrackView.findViewById(R.id.text_view_album_name);
            topTrackView.setTag(trackViewHolder);
        } else {
            trackViewHolder = (TrackViewHolder) topTrackView.getTag();
        }

        for (Image image : track.album.images) {
            if (image.height <= 64 && image.url != null) {
                Picasso.with(context)
                        .load(image.url)
                        .resize(50, 50)
                        .centerCrop()
                        .into(trackViewHolder.albumImageView);
                break;
            }
        }

        trackViewHolder.txtViewTrackName.setText(track.name);
        trackViewHolder.txtViewAlbumName.setText(track.album.name);

        return topTrackView;
    }

    @Override
    public int getCount() {
        return trackList.size();
    }


    @Override
    public Object getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trackList.indexOf(getItem(position));
    }


    public void clear() {
        trackList.clear();
    }

    public void add(Track track) {
        trackList.add(track);
    }
}
