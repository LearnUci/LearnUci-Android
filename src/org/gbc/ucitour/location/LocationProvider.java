package org.gbc.ucitour.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationProvider implements LocationListener {
  private static LocationProvider instance;
  
  private Location point;
  
  public static LocationProvider instance(Context context) {
    if (instance == null) {
      instance = new LocationProvider(context);
    }
    return instance;
  }
  
  private LocationProvider(Context context) {
    LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
  }

  public Location getLocation() {
    return point;
  }

  @Override
  public void onLocationChanged(Location location) {
    point = location;
    System.out.println(location.getLatitude() + " " + location.getLongitude());
  }

  @Override
  public void onProviderDisabled(String provider) { }

  @Override
  public void onProviderEnabled(String provider) { }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) { }
}
