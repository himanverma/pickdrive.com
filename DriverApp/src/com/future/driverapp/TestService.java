package com.future.driverapp;

import java.util.Timer;
import java.util.TimerTask;

import com.firebase.client.Firebase;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestService extends Service {

	private GPSTracker gpsTracker;
	private Handler handler = new Handler();
	private Timer timer = new Timer();
	private static final String FIREBASE_URL = "https://blazing-torch-7189.firebaseio.com/";
	private Firebase mFirebaseRef;
	public static double DISTANCE;
	boolean flag = true;
	private double totalDistance;
	Editor editor;
	SharedPreferences pref;

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		// mFirebaseRef = new Firebase(FIREBASE_URL);
		mFirebaseRef = new Firebase(FIREBASE_URL);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		editor = pref.edit();

		gpsTracker = new GPSTracker(this);
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {

					@Override
					public void run() {

						String lat = pref.getString("lat", null);
						String lng = pref.getString("lng", null);

						Toast.makeText(
								TestService.this,
								"latitude:"
										+ gpsTracker.getLocation()
												.getLatitude()
										+ "\n"
										+ "Lonlitude:"
										+ gpsTracker.getLocation()
												.getLongitude(), 4000).show();

						editor.putString("lat", String.valueOf(gpsTracker
								.getLocation().getLatitude()));
						editor.putString("lng", String.valueOf(gpsTracker
								.getLocation().getLongitude()));
						editor.commit();

						mFirebaseRef
								.child("Gmap")
								.child("lat")
								.setValue(
										gpsTracker.getLocation().getLatitude());
						mFirebaseRef
								.child("Gmap")
								.child("lng")
								.setValue(
										gpsTracker.getLocation().getLongitude());

					}
				});

			}
		};

		timer.schedule(timerTask, 0, 10000);

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		System.out
				.println("--------------------------------onDestroy -stop service ");
		timer.cancel();
		DISTANCE = totalDistance;

		editor.putString("lat", String.valueOf(""));
		editor.putString("lng", String.valueOf(""));
		editor.commit();
	}

}
