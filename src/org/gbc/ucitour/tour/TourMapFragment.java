package org.gbc.ucitour.tour;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class TourMapFragment extends SupportMapFragment implements OnMarkerClickListener {
	private LatLng latlng;
	private TourMapFragmentsActivity parent;
	private boolean launched = false;
	private GoogleMap map;
	private SharedPreferences sharedPrefs;

	public TourMapFragment() {
		super();
		//hard code in the lat and lng of aldrich park.
		latlng = new LatLng(33.646052, -117.842745);
	}

	public static TourMapFragment newInstance(LatLng customPos) {
		TourMapFragment frag = new TourMapFragment();
		frag.latlng = customPos;
		return frag;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (TourMapFragmentsActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View v = super.onCreateView(arg0, arg1, arg2);
		initMap();
		return v;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		map = getMap();
		if (map != null) {
			// Your initialization code goes here
			UiSettings settings = getMap().getUiSettings();
			// settings.setAllGesturesEnabled(false);
			settings.setRotateGesturesEnabled(false);

			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			if (!launched) {
				launched = true;
				if (latlng != null)
					getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.2f));

			}

			reDrawMap();

			getMap().setOnMapClickListener(parent);
			getMap().setOnMarkerClickListener(this);
		}
	}

	private void initMap() {
	}

	public void moveCamera(LatLng latlng, float zoom) {
		getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom), 3000, null);
	}

	public void reDrawMap() {
	}

	public void addImage(LatLng point) {

	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		return false;
	}

}