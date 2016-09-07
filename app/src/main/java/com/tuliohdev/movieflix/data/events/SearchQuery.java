package com.tuliohdev.movieflix.data.events;

/**
 * Created by tulio on 9/2/16.
 */
public class SearchQuery {
    private String query;

    public SearchQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
