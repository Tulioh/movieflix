package com.tuliohdev.movieflix.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tulio on 8/27/16.
 */
public class Movie {
    private Long id;
    private String title;
    private String year;
    private Double rating;
    private String runtime;
    private List<String> genres;
    private String summary;
    @SerializedName("description_full")
    private String descriptionFull;
    private String synopsis;
    private String language;
    @SerializedName("background_image")
    private String backgroundImageUrl;
    @SerializedName("background_image_original")
    private String backgroundImageOriginalUrl;
    @SerializedName("small_cover_image")
    private String smallCoverImageUrl;
    @SerializedName("medium_cover_image")
    private String mediumCoverImageUrl;
    @SerializedName("large_cover_image")
    private String largeCoverImageUrl;
    private List<Torrent> torrents;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public Double getRating() {
        return rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getGenreList() {
        StringBuilder genreList = new StringBuilder();

        for(String genre : genres) {
            genreList.append(genre);
            genreList.append(" ");
        }

        return genreList.toString();
    }

    public String getSummary() {
        return summary;
    }

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getLanguage() {
        return language;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public String getBackgroundImageOriginalUrl() {
        return backgroundImageOriginalUrl;
    }

    public String getSmallCoverImageUrl() {
        return smallCoverImageUrl;
    }

    public String getMediumCoverImageUrl() {
        return mediumCoverImageUrl;
    }

    public String getLargeCoverImageUrl() {
        return largeCoverImageUrl;
    }

    public List<Torrent> getTorrents() {
        return torrents;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
