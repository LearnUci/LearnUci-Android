package org.learnuci.location;

import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Location manager that can be accessed for location data
 */
public class LocationProvider implements LocationListener {
  private static LocationProvider instance;
  
  private Location point;

  //the next location in our "tour"
  private Marker nextLoc;
  
  public static LocationProvider instance(Context context) {
    if (instance == null) {
      instance = new LocationProvider(context);
    }
    return instance;
  }
  
  private LocationProvider(Context context) {
    LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, this);
  }

  /**
   * Gets the location
   * @return The location, or null if non have been found yet
   */
  public Location getLocation() {
    return point;
  }
  
  public void setNextLocation(Marker nextLoc){
	  this.nextLoc = nextLoc;
  }

  @Override
  public void onLocationChanged(Location location) {
    point = location;
    
    //if we have the next tour point, check if we're close enough
    if(nextLoc != null){
    	//if difference between two points as < X meters, bring up the imagewindow~
    	Location nextLocLoc = new Location("");
    	nextLocLoc.setLatitude(nextLoc.getPosition().latitude);
    	nextLocLoc.setLongitude(nextLoc.getPosition().longitude);
    	float meters = point.distanceTo(nextLocLoc);
    	if(meters < 75){
    		nextLoc.showInfoWindow();
    	}
    }
  }

  @Override
  public void onProviderDisabled(String provider) { }

  @Override
  public void onProviderEnabled(String provider) { }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) { }
}
