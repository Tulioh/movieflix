package com.tuliohdev.movieflix.data.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tuliohdev.movieflix.MyApplication;
import com.tuliohdev.movieflix.data.model.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tulio on 9/2/16.
 */
public class RetrofitFactory {

    public static Retrofit getInstance() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(List.class, new MovieListDeserializer());
        Gson gson = gsonBuilder.create();

        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(MyApplication.getInstance().getCacheDir(), cacheSize);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.cache(cache);
        OkHttpClient client = clientBuilder.build();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://yts.ag/api/v2/")
                .client(client)
                .build();
    }

    private static class MovieListDeserializer implements JsonDeserializer<List<Movie>> {

        @Override
        public List<Movie> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonElement content = json.getAsJsonObject().get("data").getAsJsonObject().get("movies");
            if(content != null) {
                Type listType = new TypeToken<List<Movie>>(){}.getType();
                return new Gson().fromJson(content, listType);
            } else {
                return new ArrayList<>();
            }
        }
    }
}
