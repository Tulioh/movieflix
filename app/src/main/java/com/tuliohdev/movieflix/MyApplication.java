package com.tuliohdev.movieflix;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by tulio on 8/29/16.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private Bus mEventBus = new Bus();

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public Bus getEventBus() {
        return mEventBus;
    }
}
