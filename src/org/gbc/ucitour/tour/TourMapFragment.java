package org.gbc.ucitour.tour;

import org.gbc.ucitour.R;
import org.gbc.ucitour.location.LocationProvider;
import org.gbc.ucitour.model.LocationPoint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TourMapFragment extends SupportMapFragment implements OnMarkerClickListener, OnInfoWindowClickListener {
	private LatLng latlng;
	private TourMapFragmentsActivity parent;
	private boolean launched = false;
	private GoogleMap map;
	private Marker currMarker = null;
	private LocationProvider mlocProvider;

	public TourMapFragment() {
		super();
		// hard code in the lat and lng of aldrich park.
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

		
		/* Use the LocationProvider class to obtain GPS locations */
	    mlocProvider = LocationProvider.instance(parent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View v = super.onCreateView(arg0, arg1, arg2);
		if (parent.getLps() != null) {
			// when creating, process first match
			processNextLocation();
		}
		getMap().setInfoWindowAdapter(new InfoWindowAdapter() {
			
			public View getInfoWindow(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			public View getInfoContents(Marker arg0) {
				 // Getting view from the layout file info_window_layout
	            View v = getLayoutInflater(null).inflate(R.layout.window_layout, null);
	            LocationPoint lp = parent.getLps().get(parent.currentLocation - 1);
	            TextView title = (TextView) v.findViewById(R.id.window_text);
	            ImageView image = (ImageView) v.findViewById(R.id.window_image);
	            title.setText(lp.getName());
	    		Bitmap original = BitmapFactory.decodeByteArray(lp.getImage(), 0, lp.getImage().length);
	            image.setImageBitmap(original);
	            //if this is the last marker, make sure description says so
	            if(parent.currentLocation == parent.getLps().size()){
	            	TextView desc = (TextView) v.findViewById(R.id.window_text_moveon);
	            	desc.setText("Last tour point, you're finished!");
	            }

				return v;
			}
		});
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
			settings.setCompassEnabled(true);
			settings.setTiltGesturesEnabled(false);

			if (!launched) {
				launched = true;
				if (latlng != null)
					getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.4f));
			}

			getMap().setOnMapClickListener(parent);
			getMap().setOnMarkerClickListener(this);
			getMap().setOnInfoWindowClickListener(this);
			getMap().setMyLocationEnabled(true);
		}
	}

	public void processNextLocation() {
		if (parent.currentLocation >= parent.getLps().size()) {
			// end of tour -- we can do something later here.
			return;
		}
		//remove previous point
		if(currMarker != null){
			currMarker.remove();
		}
		LocationPoint lp = parent.getLps().get(parent.currentLocation);
		float lat = lp.getLatitude();
		float lng = lp.getLongitude();
		String locTitle = lp.getName();
		
		if(getMap() != null){
			currMarker = getMap().addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(locTitle));
		}
		mlocProvider.setNextLocation(currMarker);
		parent.currentLocation++;
	}

	private void initMap() {
	}

	public void moveCamera(LatLng latlng, float zoom) {
		getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom), 3000, null);
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		processNextLocation();
	}
}