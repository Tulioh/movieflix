package com.tuliohdev.movieflix.data.model;

/**
 * Created by tulio on 8/27/16.
 */
public class Torrent {
    private String url;
    private String quality;
    private String size;
    private String seeds;
    private String peers;

    public String getUrl() {
        return url;
    }

    public String getQuality() {
        return quality;
    }

    public String getSize() {
        return size;
    }

    public String getSeeds() {
        return seeds;
    }

    public String getPeers() {
        return peers;
    }
}
