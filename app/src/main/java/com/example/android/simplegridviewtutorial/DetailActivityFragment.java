package com.example.android.simplegridviewtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by bganguly on 9/25/15.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        // The detail Activity called via intent.  Inspect the intent for movie data.
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String OWM_POSTER_PATH_PREFIX = "http://image.tmdb.org/t/p/w185";
            String extraValue;
            if (intent.hasExtra("title")) {
                extraValue = intent.getStringExtra("title");
                ((TextView) rootView.findViewById(R.id.title))
                    .setText(extraValue);
            }
            if (intent.hasExtra("poster_path")) {
                extraValue = intent.getStringExtra("poster_path");
                ImageView iconView = (ImageView) rootView.findViewById(R.id.thumbnail);
                Picasso.with(getActivity())
                        .load(OWM_POSTER_PATH_PREFIX + extraValue).into(iconView);
            }
            if (intent.hasExtra("release_date")) {
                extraValue = intent.getStringExtra("release_date");
                ((TextView) rootView.findViewById(R.id.release_date))
                        .setText(extraValue);
            }
            if (intent.hasExtra("vote_average")) {
                extraValue = intent.getStringExtra("vote_average");
                ((TextView) rootView.findViewById(R.id.vote_average))
                       .setText(extraValue+"/10");
            }
            if (intent.hasExtra("overview")) {
                extraValue = intent.getStringExtra("overview");
                ((TextView) rootView.findViewById(R.id.overview))
                        .setText(extraValue);
            }
        }

        return rootView;
    }

}
