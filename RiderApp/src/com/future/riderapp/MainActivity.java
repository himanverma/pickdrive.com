package com.future.riderapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//make class for showing driver position on map
public class MainActivity extends FragmentActivity {

	private ValueEventListener mConnectedListener;
	private static final String FIREBASE_URL = "https://blazing-torch-7189.firebaseio.com/";
	private Firebase mFirebaseRef;
	private GoogleMap googleMap;
	private SupportMapFragment mapFragment;
	Marker start, stop, marker;
	protected String add;
	protected String sub1;
	protected String sub2;
	protected String city;
	protected String state;
	protected String zip;
	protected String country;
	Editor editor;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mFirebaseRef = new Firebase(FIREBASE_URL);

		mapFragment = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map));
		googleMap = mapFragment.getMap();

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

						JSONObject obj;
						try {
							obj = new JSONObject(snapshot.getValue().toString());
							String lat1 = obj.getString("lat");
							String lng1 = obj.getString("lng");

							googleMap.moveCamera(CameraUpdateFactory
									.newLatLngZoom(
											new LatLng(
													Double.parseDouble(lat1),
													Double.parseDouble(lng1)),
											15));

							if (marker != null) {

								marker.remove();

							}

							try {
								Geocoder geocoder = new Geocoder(
										MainActivity.this, Locale.getDefault());
								List<Address> addresses = geocoder
										.getFromLocation(
												Double.parseDouble(lat1),
												Double.parseDouble(lng1), 1);
								if (addresses.size() > 0) {
									Address address1 = addresses.get(0);

									add = address1.getAddressLine(0);
									sub1 = address1.getSubAdminArea();
									sub2 = address1.getSubLocality();
									city = address1.getLocality();
									state = address1.getAdminArea();
									zip = address1.getPostalCode();
									country = address1.getCountryName();

								}
							} catch (IOException e) {
								Log.e("tag", e.getMessage());
							}

							marker = googleMap.addMarker(new MarkerOptions()
									.draggable(true)
									.position(
											new LatLng(
													Double.parseDouble(lat1),
													Double.parseDouble(lng1)))
									.title(add + "," + sub1 + "," + sub2 + ","
											+ city)
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.new_marker)));
							googleMap
									.setInfoWindowAdapter(new CustomInfoWindowAdapter(
											add, sub1, sub2, city));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
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

	private class CustomInfoWindowAdapter implements InfoWindowAdapter {

		private View view;
		String add;
		String sub1;
		String sub2;
		String city;

		public CustomInfoWindowAdapter(String add1, String sub11, String sub21,
				String city1) {
			view = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);

			add = add1;
			sub1 = sub11;
			sub2 = sub21;
			city = city1;

		}

		@Override
		public View getInfoContents(Marker marker) {

			if (MainActivity.this.marker != null
					&& MainActivity.this.marker.isInfoWindowShown()) {
				MainActivity.this.marker.hideInfoWindow();
				MainActivity.this.marker.showInfoWindow();
			}
			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			MainActivity.this.marker = marker;

			final ImageView image = ((ImageView) view.findViewById(R.id.badge));

			image.setImageResource(R.drawable.location);
			final String title = marker.getTitle();
			final TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				titleUi.setText(add + "," + sub1 + "\n" + sub2 + "," + city);
			} else {
				titleUi.setText("no data");
			}

			return view;
		}
	}

}
