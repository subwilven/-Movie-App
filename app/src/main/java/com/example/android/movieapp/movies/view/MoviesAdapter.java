package com.example.android.movieapp.movies.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieapp.POJO.Movie;
import com.example.android.movieapp.R;
import com.example.android.movieapp.movieDetails.view.DetailsActivity;
import com.example.android.movieapp.ui.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by eslam on 01-Oct-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> implements AdatperView{

    //use data from API
    private List<Movie> mData;
    private final Context context;
    private boolean useCursor;
    //use data from database
    private Cursor cursor;

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    private String buildImageUrl(String posterPath) {
        String BASIC_IMAGE_URL = "http://image.tmdb.org/t/p/";
        String IMAGE_SIZE = "w185";
        posterPath = posterPath.replace("/", "");
        Uri builderUri = Uri.parse(BASIC_IMAGE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(posterPath)
                .build();

        return builderUri.toString();

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.movie_grid_cell;
        View view = inflater.inflate(layoutID, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (useCursor) {//when using database data
            cursor.moveToPosition(position);
            holder.setMoviePoster(cursor.getString(MainActivity.COL_POSTER));

        } else {//when using data from API
            Movie movie = mData.get(position);
            holder.setMoviePoster(movie.getPoster_path());
        }

    }

    @Override
    public int getItemCount() {
        if (useCursor) {
            return cursor.getCount();
        } else {
            if (mData != null) {
                return mData.size();
            } else
                return 0;
        }

    }

    @Override
    public void sendData(List<Movie> movies) {
        this.mData = movies;
        notifyDataSetChanged();
        useCursor = false;
        if (cursor != null) {//when change the sort to anything but favorite close the cursor
            cursor.close();
            cursor = null;
        }

    }

    @Override
    public void sendData(Cursor movies) {
        this.cursor = movies;
        notifyDataSetChanged();
        if (cursor != null) {
            useCursor = true;
        }
        if(mData!=null)
        {
            mData=null;
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView moviePosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        void setMoviePoster(String posterPath) {
            String url = buildImageUrl(posterPath);
         //          Log.i("image url", url);

            Picasso.with(context).load(url).transform(new RoundedCornersTransformation(7,0)).into(moviePosterImageView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent detailsActivityIntent = new Intent(context, DetailsActivity.class);
            if (useCursor) {//when using data from database
                cursor.moveToPosition(position);

                String data =
                        cursor.getString(MainActivity.COL_TITLE) + "&&&"
                                + cursor.getString(MainActivity.COL_OVERVIEW) + "&&&"
                                + cursor.getString(MainActivity.COL_POSTER) + "&&&"
                                + cursor.getString(MainActivity.COL_VOTE_AVERAGE) + "&&&"
                                + cursor.getString(MainActivity.COL_RELEASE_DATE) + "&&&"
                                + cursor.getString(MainActivity.COL_ID);
                detailsActivityIntent.putExtra("data", data);
            } else // when using data from API

            {
                detailsActivityIntent.putExtra("data", mData.get(position));
            }

            context.startActivity(detailsActivityIntent);
        }
    }
}
