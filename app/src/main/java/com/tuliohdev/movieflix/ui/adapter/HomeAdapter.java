package com.tuliohdev.movieflix.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.tuliohdev.movieflix.R;
import com.tuliohdev.movieflix.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by tulio on 9/2/16.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemViewHolder> {

    private List<Movie> mMovies;
    private MovieListener mMovieListener;
    private Context mContext;
    private boolean mLoading;
    private int mLastMoviesCount = 0;

    public HomeAdapter() {
        this.mMovies = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        mContext = parent.getContext();

        view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_movie_home, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if(canLoadMoreMovies(position)) {
            if(mMovieListener != null) {
                mMovieListener.onLoadMoreData();
                mLastMoviesCount = mMovies.size();
                mLoading = true;
            }
        } else {
            final Movie movie = mMovies.get(position);
            holder.bindData(movie);
        }
    }

    private boolean canLoadMoreMovies(int currentPosition) {
        return !mLoading && currentPosition == mMovies.size() - 1 &&
                mLastMoviesCount != mMovies.size();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ItemViewHolder extends ViewHolder {
        @BindView(R.id.ivMoviePoster)
        ImageView ivMoviePoster;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(final Movie movie) {
            Picasso.with(mContext)
                    .load(movie.getMediumCoverImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(ivMoviePoster);

            ivMoviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMovieListener != null) {
                        mMovieListener.onMovieClicked(movie);
                    }
                }
            });
        }
    }

    public void addData(List<Movie> movieList) {
        this.mMovies.addAll(movieList);
        mLoading = false;
        notifyDataSetChanged();
    }

    public void setOnMovieClickedListener(MovieListener movieListener) {
        this.mMovieListener = movieListener;
    }

    public interface MovieListener {
        void onMovieClicked(Movie movie);
        void onLoadMoreData();
    }
}
