package com.example.youxiang.myapplication;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by youxiang on 15/9/25.
 */
public class UMApp extends Application {
    private static UMApp umapp;

    public static UMApp getApp() {
        return umapp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        umapp = this;
        Fresco.initialize(this);
    }


}
