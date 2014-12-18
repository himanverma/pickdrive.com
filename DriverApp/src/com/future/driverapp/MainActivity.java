package com.future.driverapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.future.driverapp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ValueEventListener mConnectedListener;
	private static final String FIREBASE_URL = "https://blazing-torch-7189.firebaseio.com/";
	private Firebase mFirebaseRef;
	private GoogleMap googleMap;
	boolean isGPSEnabled = false;
	protected LocationManager locationManager;
	Marker marker;
	Button start, stop;
	protected HttpResponse response;
	protected String s;
	private GPSTracker gpsTracker;
	double latitude, longitude;

	SharedPreferences prefs;
	Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// gps data
		gpsTracker = new GPSTracker(this);
		latitude = gpsTracker.getLatitude();
		longitude = gpsTracker.getLongitude();
		// local database
		prefs = getApplicationContext()
				.getSharedPreferences("Driver_Detail", 1);
		edit = prefs.edit();
       
		Log.e("id",prefs.getString("id", "0"));
		// firebase url object
		mFirebaseRef = new Firebase(FIREBASE_URL);
		// send data on server
		senddetail();

		start = (Button) findViewById(R.id.button1);
		stop = (Button) findViewById(R.id.button2);

		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

		}
		locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);

		// getting GPS status
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isGPSEnabled) {

					showSettingsAlert();

				} else {

					Intent i = new Intent(MainActivity.this, TestService.class);

					MainActivity.this.startService(i);
					Log.e("kklk", "here");
				}
			}
		});

		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, TestService.class);
				MainActivity.this.stopService(i);
			}
		});

	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public void onStart() {
		super.onStart();
		// Set up a notification to let us know when we're connected or
		// disconnected from the Firebase servers

		mFirebaseRef.child("Gmap").addValueEventListener(
				new ValueEventListener() {

					@Override
					public void onDataChange(DataSnapshot snapshot) {
						System.out.println(snapshot.getValue()); // prints
																	// "Do you have data? You'll love Firebase."
						try {
							JSONObject obj = new JSONObject(snapshot.getValue()
									.toString());
							String lat = obj.getString("lat");
							String lng = obj.getString("lng");

							Geocoder geocoder;
							List<Address> addresses;
							geocoder = new Geocoder(MainActivity.this, Locale
									.getDefault());
							addresses = geocoder.getFromLocation(
									Double.parseDouble(lat),
									Double.parseDouble(lng), 1);

							String address1 = addresses.get(0)
									.getAddressLine(0);
							String city1 = addresses.get(0).getAddressLine(1);
							String country = addresses.get(0).getAddressLine(2);

							googleMap.moveCamera(CameraUpdateFactory
									.newLatLngZoom(
											new LatLng(Double.parseDouble(lat),
													Double.parseDouble(lng)),
											15));
							googleMap.animateCamera(
									CameraUpdateFactory.zoomTo(15), 1000, null);

							googleMap.addMarker(new MarkerOptions()
									.position(
											new LatLng(Double.parseDouble(lat),
													Double.parseDouble(lng)))
									.title(address1)
									.snippet(city1)
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.location)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onCancelled(FirebaseError error) {
					}

				});

		mConnectedListener = mFirebaseRef.getRoot().child(".info/connected")
				.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						boolean connected = (Boolean) dataSnapshot.getValue();
						if (connected) {
							Toast.makeText(MainActivity.this,
									"Connected to Firebase", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(MainActivity.this,
									"Disconnected from Firebase",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancelled(FirebaseError firebaseError) {
						// No-op
					}
				});
	}

	@Override
	public void onStop() {
		super.onStop();
		// Clean up our listener so we don't have it attached twice.
		mFirebaseRef.getRoot().child(".info/connected")
				.removeEventListener(mConnectedListener);

	}

	// method for sending user detail on server
	protected void senddetail() {
		// TODO Auto-generated method stub

		AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(MainActivity.this);

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setMessage("Validating... ");
				dialog.setIndeterminate(true);
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				Account[] accounts = AccountManager.get(MainActivity.this)
						.getAccountsByType("com.google");
				String myEmailid = accounts[0].name;
				// do your background operation here
				try {
					long milli = System.currentTimeMillis();
					String url = getResources().getString(R.string.url)
							+ "drivers/addnget.json?a=" + milli;

					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

					long currentDateandTime = System.currentTimeMillis();

					if (myEmailid.equals("")) {
						entity.addPart("data[Driver][email]", new StringBody(
								"na@pickdrive.com"));

					} else {
						entity.addPart("data[Driver][email]", new StringBody(
								myEmailid));
					}
					entity.addPart("data[Driver][current_lat]", new StringBody(
							String.valueOf(latitude)));
					entity.addPart("data[Driver][current_lng]", new StringBody(
							String.valueOf(longitude)));
					entity.addPart("data[Driver][deviceId]", new StringBody(
							getDeviceDetail()));
					// device token static
					entity.addPart("data[Driver][device_token]",
							new StringBody("kkkdi7999900hhhhhhhh"));

					httppost.setEntity(entity);

					response = httpclient.execute(httppost);

					s = EntityUtils.toString(response.getEntity());
					// Log.e("fhgfhj", s);

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// what to do when background task is completed
				try {

					JSONObject obj = new JSONObject(s);
					JSONObject data = obj.getJSONObject("data");

					String err = data.getString("error");
					String msg = data.getString("msg");
					
					if (err.equals("0")) {
						
						JSONObject driver = data.getJSONObject("Driver");
						String id = driver.getString("id");
						String full_name = driver.getString("full_name");
						String deviceId = driver.getString("deviceId");
						String email = driver.getString("email");
						String contact_number = driver
								.getString("contact_number");
						String current_lat = driver.getString("current_lat");
						String current_lng = driver.getString("current_lng");
						String photo_path = driver.getString("photo_path");

						edit.putString("id", id);
						edit.putString("full_name", full_name);
						edit.putString("deviceId", deviceId);
						edit.putString("email", email);
						edit.putString("contact_number", contact_number);
						edit.putString("current_lat", current_lat);
						edit.putString("current_lng", current_lng);
						edit.putString("photo_path", photo_path);
						edit.commit();
					}

					
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.cancel();
			}

		};
		if ((DetectNetwork.hasConnection(getApplicationContext())))
			updateTask.execute((Void[]) null);

	}

	String getDeviceDetail() {

		return Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

	}

}
