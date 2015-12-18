package com.example.android.simplegridviewtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by bganguly on 9/25/15.
 */
public class MainActivityFragment extends Fragment {
    ImageAdapter mImageAdapter;
    List mMovieDataMapArrayList;
    String mSortOrder;

    public MainActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
         Bundle savedInstanceState) {
        mImageAdapter =
                new ImageAdapter(getActivity(), android.R.layout.simple_list_item_1);
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mImageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String OWM_POSTER_PATH_PREFIX = "http://image.tmdb.org/t/p/w185";
                Object adapterItemAtPosition = mImageAdapter.getItem(position);
                Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
                    .putExtra("title",
                        ((HashMap) adapterItemAtPosition).get("title").toString())
                    .putExtra("poster_path",
                        OWM_POSTER_PATH_PREFIX +
                        ((HashMap) adapterItemAtPosition).get("poster_path").toString())
                    .putExtra("release_date",
                        ((HashMap) adapterItemAtPosition).get("release_date").toString())
                    .putExtra("vote_average",
                        ((HashMap) adapterItemAtPosition).get("vote_average").toString())
                    .putExtra("overview",
                        ((HashMap) adapterItemAtPosition).get("overview").toString());
                startActivity(detailActivityIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_by_popularity) {
            mMovieDataMapArrayList = getImageUrlsListAsync(mImageAdapter, "popularity.desc");
        } else if (id == R.id.action_sort_by_rating) {
            mMovieDataMapArrayList = getImageUrlsListAsync(mImageAdapter, "vote_average.desc");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMovieDataMapArrayList = getImageUrlsListAsync( mImageAdapter, "popularity.desc");
    }

    public List getImageUrlsListAsync(ImageAdapter mImageAdapter, String sortOrder) {
        FetchImagesTask fetchImagesTask = new FetchImagesTask(mImageAdapter);
        fetchImagesTask.execute(sortOrder);
        return fetchImagesTask.getMovieDataMapArrayList();
    }
}
