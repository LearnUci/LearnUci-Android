package org.learnuci.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.learnuci.model.LocationPoint;
import org.learnuci.model.TourPointInfo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class Query {
  private static final String QUERY_URL = "http://learnuci.appspot.com/query";
  private Query() { }
  
  public static List<LocationPoint> query(final String action, final String type, final String value) {
    JsonParser parser = new JsonParser();
    JsonArray arr = (JsonArray) parser
        .parse(NetworkDispatcher.get(QUERY_URL, new HashMap<String, String>() {{
          put("action", action);
          put("type", type);
          put("value", value);
        }}));
    List<LocationPoint> locations = new ArrayList<LocationPoint>();
    for (JsonElement elem : arr) {
      locations.add(new LocationPoint((JsonObject) elem));
    }
    return locations;
  }	
  
  public static List<TourPointInfo> queryTours(){
	    JsonParser parser = new JsonParser();
	    JsonArray arr = (JsonArray) parser
	        .parse(NetworkDispatcher.get(QUERY_URL, new HashMap<String, String>() {{
	          put("action", "TOURS");
	        }}));
	    List<TourPointInfo> tours = new ArrayList<TourPointInfo>();
	    for (JsonElement elem : arr) {
	      tours.add(new TourPointInfo((JsonObject) elem));
	    }
	    return tours;
  }
  
  public static LocationPoint single(final String type, final String value) {
    List<LocationPoint> resp = query("MATCH", type, value);
    if (resp.size() != 1) {
      throw new IllegalArgumentException(
          "Response did not contain exactly 1 element, contained " + resp.size());
    }
    return resp.get(0);
  }
}
