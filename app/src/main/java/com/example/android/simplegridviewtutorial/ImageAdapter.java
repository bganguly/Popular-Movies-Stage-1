package com.example.android.simplegridviewtutorial;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by bganguly on 9/28/15.
 */
public class ImageAdapter extends ArrayAdapter {
    private Context mContext;

    public ImageAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        String OWM_POSTER_PATH = "poster_path";
        String OWM_POSTER_PATH_PREFIX = "http://image.tmdb.org/t/p/w185";
        String poster_path = ((HashMap)getItem(position)).get(OWM_POSTER_PATH).toString();
        Picasso.with(mContext).load(OWM_POSTER_PATH_PREFIX + poster_path).into(imageView);
        return imageView;
    }

}

