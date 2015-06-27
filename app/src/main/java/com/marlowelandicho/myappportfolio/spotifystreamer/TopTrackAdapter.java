package com.marlowelandicho.myappportfolio.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marlowelandicho.myappportfolio.spotifystreamer.data.SpotifyStreamerTrack;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by marlowe.landicho on 24/6/15.
 */
public class TopTrackAdapter extends BaseAdapter {

    private final String LOG_TAG = TopTrackAdapter.class.getSimpleName();
    private final Context context;
    private final List<SpotifyStreamerTrack> trackList;

    public TopTrackAdapter(Context context, List<SpotifyStreamerTrack> trackList) {
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

        SpotifyStreamerTrack track = (SpotifyStreamerTrack) getItem(position);

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

        if (track.getThumbnailUrl() != null) {
            Picasso.with(context)
                    .load(track.getThumbnailUrl())
                    .resize(50, 50)
                    .centerCrop()
                    .into(trackViewHolder.albumImageView);

        }
        trackViewHolder.txtViewTrackName.setText(track.getArtistName());
        trackViewHolder.txtViewAlbumName.setText(track.getAlbumName());

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

    public void add(SpotifyStreamerTrack track) {
        trackList.add(track);
    }
}
