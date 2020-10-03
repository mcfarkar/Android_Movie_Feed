package com.codepath.mcfarkar.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.mcfarkar.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyDH_Jx7IWGwux5DtSfgN4g9R68zymsCwUE";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    // Declare variables to associate view items and intent data
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        // to receive data in intent

        // example to get items manually
        // String title = getIntent().getStringExtra("title");
        // however, more efficient to grab entire move object, as shown below

        // Parceler allows us to pass in the whole move in an intent and have access to all of its attributes
        final Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());




        // Create an instance of a client
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                  // get the array called results from the json object
                // handle for the case where the results array does not exist by using a try/catch
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0) {
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d( "DetailActivity", youtubeKey);


                    initializeYoutube(youtubeKey, movie.getRating());

                } catch (JSONException e) {
                    Log.e( "DetailActivity", "Failed to parse JSON" , e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });





    }

    private void initializeYoutube(final String youtubeKey, final double moviePopularity) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "onInitializationSuccess");

                // do any work here to cue video, play video, etc.

                if (moviePopularity < 6.5)
                {
                    // show an image preview that can initiate playing a YouTube video.
                    youTubePlayer.cueVideo(youtubeKey);
                }
                else {

                    //  otherwise when clicking on a popular movie (i.e. 5+ stars) the video should be played immediately.
                    youTubePlayer.loadVideo(youtubeKey);
                }


            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity", "onInitializationFailure");
            }
        });
    }
}