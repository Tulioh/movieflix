package com.tuliohdev.movieflix.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tuliohdev.movieflix.R;
import com.tuliohdev.movieflix.data.model.Movie;
import com.tuliohdev.movieflix.data.model.Torrent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by tulio on 9/2/16.
 */
public class MovieDetailFragment extends Fragment {

    private static final String MOVIE_KEY = "MOVIE_KEY";

    @BindView(R.id.rootView)
    LinearLayout mRootView;
    @BindView(R.id.imgMovieBackground)
    ImageView mImgMovieBackground;
    @BindView(R.id.txtTitle)
    TextView mTxtTitle;
    @BindView(R.id.txtMovieYear)
    TextView mTxtMovieYear;
    @BindView(R.id.txtMovieRuntime)
    TextView mTxtMovieRuntime;
    @BindView(R.id.txtMovieGenres)
    TextView mTxtMovieGenres;
    @BindView(R.id.txtMovieDescription)
    TextView mTxtMovieDescription;
    @BindView(R.id.btnHD)
    Button mBtnHD;
    @BindView(R.id.btnFullHD)
    Button mBtnFullHD;

    private Unbinder mButterknifeUnbinder;
    private Movie mMovie;
    private String mHdLink;
    private String mFullHdLink;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_KEY, movie.toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovie = new Gson().fromJson(getArguments().getString(MOVIE_KEY), Movie.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mButterknifeUnbinder = ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents() {
        Picasso.with(getActivity())
                .load(mMovie.getBackgroundImageOriginalUrl())
                .into(mImgMovieBackground);

        mTxtTitle.setText(mMovie.getTitle());
        mTxtMovieYear.setText(mMovie.getYear());
        mTxtMovieRuntime.setText(mMovie.getRuntime() + " m");
        mTxtMovieGenres.setText(mMovie.getGenreList());
        mTxtMovieDescription.setText(mMovie.getSynopsis());

        for(Torrent torrent : mMovie.getTorrents()) {
            if(torrent.getQuality().equals("720p")) {
                mBtnHD.setVisibility(View.VISIBLE);
                mHdLink = torrent.getUrl();
            } else if(torrent.getQuality().equals("1080p")) {
                mBtnFullHD.setVisibility(View.VISIBLE);
                mFullHdLink = torrent.getUrl();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mButterknifeUnbinder.unbind();
    }

    @OnClick(R.id.btnHD)
    void onClickCopyHDLink() {
        copyTextToClipboard(mHdLink);
    }

    @OnClick(R.id.btnFullHD)
    void onClickCopyFullHDLink() {
        copyTextToClipboard(mFullHdLink);
    }

    private void copyTextToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("torrent-link", text);
        clipboard.setPrimaryClip(clip);

        Snackbar.make(mRootView, R.string.torrent_link_copied_successful,
                Snackbar.LENGTH_LONG).show();
    }
}
