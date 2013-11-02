package org.gbc.ucitour.tour;

import java.util.List;

import org.gbc.ucitour.R;
import org.gbc.ucitour.model.LocationPoint;
import org.gbc.ucitour.net.Query;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TourMapFragmentsActivity extends FragmentActivity implements OnMapClickListener {

	private static final String MAP_FRAGMENT_TAG = "GoogleMap";
	private static final String COLOR_UNSELECTED = "#27ae60";
	private static final int SELECTED_GRADIENT_RES = R.drawable.green_button_pressed;
	private TourMapFragment mMapFragment;
	private Button mapModeButton;
	private Button arModeButton;
	private List<LocationPoint> lps;
	public int currentLocation = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_fragments_main);

		lps = Query.query("EXPAND_TOUR", "\"\"", getIntent().getLongExtra("tourId", 0) + "");
		
		mMapFragment = (TourMapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
		if (mMapFragment == null) {
			// To programmatically add the map, we first create a
			// SupportMapFragment.
			mMapFragment = new TourMapFragment();
			// Then we add it using a FragmentTransaction.
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.fragment_container, mMapFragment, MAP_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mapModeButton = (Button) findViewById(R.id.tour_map_fragment_button);
		mapModeButton.setClickable(false);
		arModeButton = (Button) findViewById(R.id.tour_ar_fragment_button);

		// set up background colors
		mapModeButton.setBackgroundResource(SELECTED_GRADIENT_RES);
		arModeButton.setBackgroundColor(Color.parseColor(COLOR_UNSELECTED));

		mapModeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// make arModeButton unselected
				arModeButton.setBackgroundColor(Color.parseColor(COLOR_UNSELECTED));
				// make mapModeButton selected
				mapModeButton.setBackgroundResource(SELECTED_GRADIENT_RES);

				arModeButton.setClickable(true);
				v.setClickable(false);

				replaceWithMap();
			}
		});

		arModeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// make mapModeButton unselected
				mapModeButton.setBackgroundColor(Color.parseColor(COLOR_UNSELECTED));
				// make arModeButton selected
				arModeButton.setBackgroundResource(SELECTED_GRADIENT_RES);

				mapModeButton.setClickable(true);
				v.setClickable(false);

				replaceWithAR();
			}
		});

	}

	public List<LocationPoint> getLps(){
		return lps;
	}

	private void replaceWithAR() {
		// TODO Auto-generated method stub

	}

	private void replaceWithMap() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}
}