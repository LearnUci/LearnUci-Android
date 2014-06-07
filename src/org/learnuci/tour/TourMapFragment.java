package org.learnuci.tour;

import java.util.Locale;

import org.learnuci.R;
import org.learnuci.location.LocationProvider;
import org.learnuci.model.LocationPoint;
import org.learnuci.view.ViewHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TourMapFragment extends SupportMapFragment implements OnMarkerClickListener, OnInfoWindowClickListener, TextToSpeech.OnInitListener {
	private LatLng latlng;
	private TourMapFragmentsActivity parent;
	private boolean launched = false;
	private boolean ready = false;
	private GoogleMap map;
	private Marker currMarker = null;
	private LocationProvider mlocProvider;
	private TextToSpeech tts;

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
		if (tts == null) {
      tts = new TextToSpeech(getActivity(), this);
    }
	}
	
	@Override
	public void onDestroy() {
	  super.onDestroy();
	  if (tts != null) {
	    tts.stop();
	    tts.shutdown();
	  }
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
	            title.setText(lp.getName());
	            if (lp.hasImage()) {
	              ImageView image = (ImageView) v.findViewById(R.id.window_image);
	              Bitmap original = BitmapFactory.decodeByteArray(lp.getImage(), 0, lp.getImage().length);
	              image.setImageBitmap(original);
	            }
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
		  parent.finish();
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
	  LocationPoint point = parent.getLps().get(parent.currentLocation - 1);
	  if (point.hasDescription()) {
	    if (ready) {
	      String[] parts = point.getDescription().split("\\|");
	      String desc = "";
	      for (int i = 0; i < parts.length; i++) {
	        tts.speak(parts[i], TextToSpeech.QUEUE_ADD, null);
	        desc += parts[i];
	        if (i < parts.length - 1) {
	          desc += ". ";
	        }
	      }
	      
	      
	      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	      builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	          if (tts.isSpeaking()) {
	            tts.stop();
	          }
	          processNextLocation();
	        }
	      });
	      
	      TextView text = new TextView(getActivity());
	      text.setText(desc);
	      
	      ScrollView scroll = new ScrollView(getActivity());
	      scroll.addView(text);
	      scroll.setLayoutParams(ViewHelper.linearLayout(ViewHelper.MATCH_PARENT, ViewHelper.MATCH_PARENT));
	      scroll.setPadding(ViewHelper.dpInt(15, getActivity()), ViewHelper.dpInt(15, getActivity()),
	          ViewHelper.dpInt(15, getActivity()), ViewHelper.dpInt(15, getActivity()));
	      builder.setView(scroll);
	      builder.setTitle(point.getName());
  	    AlertDialog dialog = builder.create();
  	    dialog.show();
	    }
	  } else {
	    processNextLocation();
	  }
	}

  @Override
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      // Sounds better than normal english
      int result = tts.setLanguage(Locale.UK);
      if (result == TextToSpeech.LANG_MISSING_DATA
          || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        result = tts.setLanguage(Locale.ENGLISH);
        if (result == TextToSpeech.LANG_MISSING_DATA
            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
          Toast.makeText(getActivity(), "Unable to narrate tour", Toast.LENGTH_LONG).show();
          return;
        }
      }
      ready = true;
    } else {
      Toast.makeText(getActivity(), "Unable to narrate tour", Toast.LENGTH_LONG).show();
    }
  }
}