package com.example.android.simplegridviewtutorial;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bganguly on 9/25/15.
 */
public class FetchImagesTask extends AsyncTask<String, Void, List> {
    private final String LOG_TAG = FetchImagesTask.class.getSimpleName();
    String mPopularMoviesJsonStr = null;
    ArrayAdapter<String> mImageAdapter;
    private List mMovieDataMapArrayList;

    public FetchImagesTask(ArrayAdapter<String> imageAdapter) {
        mImageAdapter = imageAdapter;
    }

    /**
     * Take the String representing the complete moviedb response in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public List getMovieDataMapArrayList(String moviedbJsonStr) throws JSONException {
        List movieDataMapArrayList = new ArrayList();

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "results";

        JSONObject moviedbJson = new JSONObject(moviedbJsonStr);
        JSONArray moviedbArray = moviedbJson.getJSONArray(OWM_LIST);
        int weatherArrayLength = moviedbArray.length();

        for(int i = 0; i < weatherArrayLength; i++) {
            HashMap moviedbJsonMap = new HashMap<>();
            // Get the JSON object representing the day
            JSONObject individualMovieJson = moviedbArray.getJSONObject(i);
            moviedbJsonMap.put("adult", individualMovieJson.get("adult"));
            moviedbJsonMap.put("backdrop_path", individualMovieJson.get("backdrop_path"));
            moviedbJsonMap.put("genre_ids", individualMovieJson.get("genre_ids"));
            moviedbJsonMap.put("id", individualMovieJson.get("id"));
            moviedbJsonMap.put("original_language", individualMovieJson.get("original_language"));
            moviedbJsonMap.put("original_title", individualMovieJson.get("original_title"));
            moviedbJsonMap.put("overview", individualMovieJson.get("overview"));
            moviedbJsonMap.put("release_date", individualMovieJson.get("release_date"));
            moviedbJsonMap.put("poster_path", individualMovieJson.get("poster_path"));
            moviedbJsonMap.put("popularity", individualMovieJson.get("popularity"));
            moviedbJsonMap.put("title", individualMovieJson.get("title"));
            moviedbJsonMap.put("video", individualMovieJson.get("video"));
            moviedbJsonMap.put("vote_average", individualMovieJson.get("vote_average"));
            moviedbJsonMap.put("vote_count", individualMovieJson.get("vote_count"));
            movieDataMapArrayList.add(moviedbJsonMap);
        }

        return movieDataMapArrayList;

    }

    @Override
    protected List doInBackground(String... params) {

        // Verify size of param and return early, if size is zero
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String popularMoviesJsonStr = null;
        String sortByParamValue = params[0];
        String apiKeyParamValue = "myFakeApiKey";

        try {
            // Construct the URL for the moviedb query using Uri.build()
            // Possible parameters are available at
            // http://docs.themoviedb.apiary.io/#reference/discover/get?console=1
            final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, sortByParamValue)
                    .appendQueryParameter(API_KEY_PARAM,apiKeyParamValue)
                    .build();

            URL uri = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            popularMoviesJsonStr = buffer.toString();
            mPopularMoviesJsonStr = popularMoviesJsonStr;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataMapArrayList(popularMoviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error Parsing JSON", e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List result) {
        if (result != null) {
            mImageAdapter.clear();
            mImageAdapter.addAll(result);
            // New data is back from the server.  Hooray!
        }
    }

    public List getMovieDataMapArrayList() {
        return mMovieDataMapArrayList;
    }
}
