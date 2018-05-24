package com.heavenlyhell.instamemo.utils;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sande on 3/7/2018.
 */

public class AppInitiation extends Application {

    private static final String TAG = "AppInitiation";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: App initiation.");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
