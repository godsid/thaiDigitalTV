package com.webmanagement.thaidigitaltv;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class MyApplication extends Application {

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
    }
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if (trackerId == TrackerName.APP_TRACKER) {
                Tracker t = analytics.newTracker(R.xml.app_tracker);
                mTrackers.put(trackerId, t);
            }
        }
        return mTrackers.get(trackerId);
    }
}