package org.gbc.ucitour.tour;

import java.util.ArrayList;
import java.util.List;

import org.gbc.ucitour.R;
import org.gbc.ucitour.ar.AugmentedRealityFragment;
import org.gbc.ucitour.model.LocationPoint;
import org.gbc.ucitour.net.Query;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapsInitializer;
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
  private List<LocationPoint> lps;
  public int currentLocation = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tour_fragments_main);
    if (getIntent().hasExtra("tourId")) {
      lps = Query.query("EXPAND_TOUR", "\"\"", getIntent().getLongExtra("tourId", 0) + "");
    } else {
      lps = new ArrayList<LocationPoint>();
      lps.add(Query.single("id", getIntent().getLongExtra("locationId", 0) + ""));
    }
    
    mMapFragment = addFragment(MAP_FRAGMENT_TAG, TourMapFragment.class);
    arFragment = addFragment(AR_FRAGMENT_TAG, AugmentedRealityFragment.class);
    
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(arFragment);
    fragmentTransaction.commit();

    try {
      MapsInitializer.initialize(this);
    } catch (GooglePlayServicesNotAvailableException e) {
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
        updateLocation();
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

  public void updateLocation() {
    arFragment.setLocation(lps.get(currentLocation - 1));
  }
  
  private <T extends Fragment> T addFragment(String tag, Class<T> clazz) {
    try {
      Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
      if (f == null) {
        T frag = clazz.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, frag, tag);
        fragmentTransaction.commit();
        return frag;
      }
      return clazz.cast(f);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void replaceWithAR() {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(mMapFragment);
    fragmentTransaction.show(arFragment);
    fragmentTransaction.commit();
  }

  private void replaceWithMap() {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.hide(arFragment);
    fragmentTransaction.show(mMapFragment);
    fragmentTransaction.commit();
  }

  public List<LocationPoint> getLps() {
    return lps;
  }

  @Override
  public void onMapClick(LatLng arg0) {
  }
}