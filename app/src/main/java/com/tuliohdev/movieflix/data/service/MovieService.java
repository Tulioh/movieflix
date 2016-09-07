package com.tuliohdev.movieflix.data.service;

import com.tuliohdev.movieflix.data.model.Movie;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tulio on 8/27/16.
 */
public interface MovieService {

    @GET("list_movies.json")
    Observable<List<Movie>> getAllMovies(@Query("page") int pageNumber);

    @GET("list_movies.json")
    Observable<List<Movie>> filterMovie(@Query("query_term") String query, @Query("page") int pageNumber);

    @GET("list_movies.json")
    Observable<List<Movie>> getAllMoviesByGenre(@Query("genre") String genre, @Query("page") int pageNumber);
}
