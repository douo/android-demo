/*
 * Copyright (c) 2015. Tiou Lims, All rights reserved.
 */

package info.dourok.android.demo.services;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import info.dourok.android.demo.LogUtils;

/**
 * Created by DouO on 7/23/15.
 */
public class ServiceDemo extends Service {

    @Override
    public void onCreate() {
        d("onCreate");
    }

    @Override
    public void onDestroy() {
        d("onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        d("onStartCommand:" + startId + " flags:" + flags);
        d(LogUtils.dumpIntent(intent));
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        d("onConfigurationChanged:" + newConfig);
    }

    @Override
    public void onLowMemory() {
        d("onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        d("onTrimMemory:" + level);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        d("onTaskRemoved:" + LogUtils.dumpIntent(rootIntent));
    }

    @Override
    public void onStart(Intent intent, int startId) {
        d("onStart:" + startId);
        d(LogUtils.dumpIntent(intent));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        d("onBind");
        d(LogUtils.dumpIntent(intent));
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        d("onUnbind");
        d(LogUtils.dumpIntent(intent));
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        d("onRebind");
        d(LogUtils.dumpIntent(intent));
    }

    public void d(Object o) {
        Log.d("ServiceDemo", o == null ? "null" : o.toString());
    }
}
