package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Service;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class DisplayAlertTime extends Service {


    @Override
    public void onCreate() {
        Log.d("run","onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("run","onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("run","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("run","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("run","onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        Log.d("run","onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d("run","onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("run","onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("run","onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("run","onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("run","onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }
}
