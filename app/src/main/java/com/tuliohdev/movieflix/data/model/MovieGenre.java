package com.tuliohdev.movieflix.data.model;

/**
 * Created by tulio on 9/7/16.
 */
public enum MovieGenre {
    NONE(""),
    ACTION("action"),
    ADVENTURE("adventure"),
    ANIMATION("animation"),
    COMEDY("comedy"),
    CRIME("crime"),
    DRAMA("drama"),
    HORROR("horror"),
    SCIFI("sci-fi");

    private String genre;

    MovieGenre(String genre) {
        this.genre = genre;
    }

    public String getValue() {
        return genre;
    }
}
