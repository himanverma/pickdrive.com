package com.future.driverapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * @author mimming
 * @since 12/5/14.
 *
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class DrawingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
