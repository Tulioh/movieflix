package com.tuliohdev.movieflix.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tuliohdev.movieflix.R;
import com.tuliohdev.movieflix.data.model.Movie;
import com.tuliohdev.movieflix.data.model.MovieGenre;
import com.tuliohdev.movieflix.data.service.MovieService;
import com.tuliohdev.movieflix.data.service.RetrofitFactory;
import com.tuliohdev.movieflix.ui.adapter.HomeAdapter;
import com.tuliohdev.movieflix.ui.adapter.HomeAdapter.MovieListener;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tulio on 8/27/16.
 */
public class MoviesFragment extends Fragment implements MovieListener {

    private static final String MOVIE_FILTER_QUERY_KEY = "MOVIE_FILTER_QUERY_KEY";
    private static final String MOVIE_CATEGORY_KEY = "MOVIE_CATEGORY_KEY";

    @BindView(R.id.rootView) RelativeLayout mRootView;
    @BindView(R.id.rvMovies) RecyclerView mRvMovies;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private Unbinder mButterknifeUnbinder;
    private Retrofit mRetrofit;
    private HomeAdapter mHomeAdapter;
    private int mCurrentMoviePageNumber = 1;
    private String mFilterQuery;
    private MovieGenre mMovieGenre;

    public MoviesFragment() {}

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    public static MoviesFragment newInstance(MovieGenre movieGenre) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_CATEGORY_KEY, movieGenre.toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MoviesFragment newInstance(String filterQuery) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_FILTER_QUERY_KEY, filterQuery);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mFilterQuery = getArguments().getString(MOVIE_FILTER_QUERY_KEY, null);

            String movieCategory = getArguments().getString(MOVIE_CATEGORY_KEY);
            if(movieCategory != null) {
                mMovieGenre = MovieGenre.valueOf(movieCategory);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mButterknifeUnbinder = ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents() {
        mRetrofit = RetrofitFactory.getInstance();

        mRvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mHomeAdapter = new HomeAdapter();
        mHomeAdapter.setOnMovieClickedListener(MoviesFragment.this);
        mRvMovies.setAdapter(mHomeAdapter);

        loadMoviesFromServer();
    }

    private void loadMoviesFromServer() {
        mProgressBar.setVisibility(View.VISIBLE);

        Observable<List<Movie>> observable;

        if(mFilterQuery != null) {
            observable = mRetrofit.create(MovieService.class)
                    .filterMovie(mFilterQuery, mCurrentMoviePageNumber);
        } else if(mMovieGenre != null) {
            observable = mRetrofit.create(MovieService.class)
                    .getAllMoviesByGenre(mMovieGenre.getValue(), mCurrentMoviePageNumber);
        } else {
            observable = mRetrofit.create(MovieService.class)
                    .getAllMovies(mCurrentMoviePageNumber);
        }

        observable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<Movie>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    if(!MoviesFragment.this.isVisible()) return;

                    mProgressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(getActivity())
                            .setMessage(e.getMessage())
                            .setPositiveButton(getString(R.string.ok), null)
                            .create().show();
                }

                @Override
                public void onNext(List<Movie> movies) {
                    if(!MoviesFragment.this.isVisible()) return;

                    mProgressBar.setVisibility(View.GONE);
                    mHomeAdapter.addData(movies);
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mButterknifeUnbinder.unbind();
    }

    @Override
    public void onMovieClicked(Movie movie) {
        ((MainActivity) getActivity()).replaceContent(
                MovieDetailFragment.newInstance(movie), true);
    }

    @Override
    public void onLoadMoreData() {
        mCurrentMoviePageNumber++;
        loadMoviesFromServer();
    }
}
