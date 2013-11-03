package org.gbc.ucitour.tour;

import org.gbc.ucitour.R;
import org.gbc.ucitour.ar.AugmentedRealityFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;

public class TourMapFragmentsActivity extends FragmentActivity implements OnMapClickListener {
  private static final String MAP_FRAGMENT_TAG = "GoogleMap";
  private static final String AR_FRAGMENT_TAG = "AR";
  private static final String COLOR_UNSELECTED = "#27ae60";
  private static final int SELECTED_GRADIENT_RES = R.drawable.green_button_pressed;
	private TourMapFragment mMapFragment;
	private AugmentedRealityFragment arFragment;
	private Button mapModeButton;
	private Button arModeButton;

	@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tour_fragments_main);
        
		
		
		arFragment = (AugmentedRealityFragment) getSupportFragmentManager().findFragmentByTag(AR_FRAGMENT_TAG);
		if (arFragment == null) {
		  arFragment = new AugmentedRealityFragment();
		  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		  fragmentTransaction.add(R.id.fragment_container, arFragment, AR_FRAGMENT_TAG);
      fragmentTransaction.commit();
		}
		
		mMapFragment = (TourMapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
    if (mMapFragment == null) {
      // To programmatically add the map, we first create a SupportMapFragment.
      mMapFragment = new TourMapFragment();
      // Then we add it using a FragmentTransaction.
      FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.add(R.id.fragment_container, mMapFragment, MAP_FRAGMENT_TAG);
      fragmentTransaction.hide(mMapFragment);
      fragmentTransaction.commit();
    }
		
		mapModeButton = (Button) findViewById(R.id.tour_map_fragment_button);
		mapModeButton.setClickable(false);
		arModeButton = (Button) findViewById(R.id.tour_ar_fragment_button);
		
		//set up background colors
		mapModeButton.setBackgroundResource(SELECTED_GRADIENT_RES);
		arModeButton.setBackgroundColor(Color.parseColor(COLOR_UNSELECTED));
		
		mapModeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//make arModeButton unselected
				arModeButton.setBackgroundColor(Color.parseColor(COLOR_UNSELECTED));
				//make mapModeButton selected
				mapModeButton.setBackgroundResource(SELECTED_GRADIENT_RES);
				
				arModeButton.setClickable(true);
				v.setClickable(false);
				replaceWithMap();
			}

		});
		
		arModeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//make mapModeButton unselected
				mapModeButton.setBackgroundColor(Color.parseColor(COLOR_UNSELECTED));
				//make arModeButton selected
				arModeButton.setBackgroundResource(SELECTED_GRADIENT_RES);
				
				mapModeButton.setClickable(true);
				v.setClickable(false);
				replaceWithAR();
			}
		});
  }
	
	private void replaceWithAR() {
	  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(arFragment);
    fragmentTransaction.show(mMapFragment);
	  fragmentTransaction.commit();
	}
	
	private void replaceWithMap() {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(mMapFragment);
    fragmentTransaction.show(arFragment);
    fragmentTransaction.commit();
	}
	
	@Override
	public void onMapClick(LatLng arg0) { }
}