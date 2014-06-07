package org.learnuci.model;

import android.util.Base64;

import com.google.gson.JsonObject;

/**
 * Datastructure to represent a location point.
 */
public class LocationPoint {
  private float lat;
  private float lng;
  private String name;
  private String abbr;
  private String type;
  private String desc = null;
  private byte[] img;
  private long id;
  
  /**
   * @param obj Json object that contains the data to wrap into a LocationPoint
   */
  public LocationPoint(JsonObject obj) {
    name = obj.get("name").getAsString();
    abbr = obj.get("abbr").getAsString();
    lat = Float.valueOf(obj.get("lat").getAsString());
    lng = Float.valueOf(obj.get("lng").getAsString());
    type = obj.get("type").getAsString();
    if (obj.has("img")) {
      img = Base64.decode(obj.get("img").getAsString(), Base64.DEFAULT);
    }
    if (obj.has("desc")) {
      desc = obj.get("desc").getAsString();
    }
    float newLat = (0.120246f * lat) + (-0.0460953f * lng) + 24.1819f;
    float newLng = (0.0693213f * lat) + (0.118713f * lng) - 106.18865f;
    lat = newLat;
    lng = newLng;
    id = obj.get("id").getAsLong();
  }
  
  public String getName() {
    return name;
  }
  
  public String getAbbreviation() {
    return abbr;
  }
  
  public float getLatitude() {
    return lat;
  }
  
  public float getLongitude() {
    return lng;
  }
  
  public String getType() {
    return type;
  }
  
  public boolean hasImage() {
    return img != null;
  }
  
  public byte[] getImage() {
    return img;
  }
  
  public boolean hasDescription() {
    return desc != null;
  }
  
  public String getDescription() {
    return desc;
  }
  
  public long getId() {
    return id;
  }
}
