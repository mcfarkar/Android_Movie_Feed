package com.codepath.mcfarkar.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.mcfarkar.flixster.DetailActivity;
import com.codepath.mcfarkar.flixster.MainActivity;
import com.codepath.mcfarkar.flixster.R;
import com.codepath.mcfarkar.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by mcfarkar on 24,September,2020
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the movie at the passed in position
        // Get the data model based on position
        Movie movie = movies.get(position);

        // Bind the movie data into the viewholder
        // Set item views based on your views and data model
        holder.bind(movie);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imageUrl;

            // if phone in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // then assign backdrop image
                imageUrl = movie.getBackdropPath();
            } else {
                // else assign poster image
                imageUrl = movie.getPosterPath();
            }
            Glide.with(context).load(imageUrl).placeholder(R.drawable.comingsoon).into(ivPoster);

            // 1. Register the click listener on the whole container
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 2. Navigate to a new activity on tap using an intent

                        // Initially used a toast to make sure the correct on click activity was occurring
                        // Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();

                    // provide an Intent (pkg context, the activity we want to navigate to)
                    // this allows us to go to the Detail Activity when we click on the movie area
                    Intent i = new Intent(context, DetailActivity.class);

                    // to pass information with the intent


                    // example to get items manually
                    // i.putExtra("title", movie.getTitle());
                    // however, more efficient to grab entire move object, as shown below

                    i.putExtra("movie", Parcels.wrap(movie));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context,(View)ivPoster, "movie");

                     context.startActivity(i, options.toBundle());


                //   context.startActivity(i);

                }
            });
        }
    }

}
