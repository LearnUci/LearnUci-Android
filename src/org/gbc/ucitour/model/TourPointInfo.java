package org.gbc.ucitour.model;

import java.util.ArrayList;

import android.util.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TourPointInfo {
	//name of place
	private String tpName;
	//description
	private String tpDesc;
	//image
	private byte[] tpImg;
	//list of points
	private ArrayList<LocationPoint> locationPoints;
	
	public TourPointInfo(JsonObject tpInfo){
		tpName = tpInfo.get("name").getAsString();
		tpDesc = tpInfo.get("desc").getAsString();
	    if (tpInfo.has("img")) {
	    	tpImg = Base64.decode(tpInfo.get("img").getAsString(), Base64.DEFAULT);
	    }
	    locationPoints = new ArrayList<LocationPoint>();
	    JsonArray lps = tpInfo.get("locations").getAsJsonArray();
	    for(JsonElement e : lps){
	    	locationPoints.add(new LocationPoint(e.getAsJsonObject()));
	    }
	}

	public String getTpName() {
		return tpName;
	}

	public void setTpName(String tpName) {
		this.tpName = tpName;
	}

	public String getTpDesc() {
		return tpDesc;
	}

	public void setTpDesc(String tpDesc) {
		this.tpDesc = tpDesc;
	}

	public byte[] getTpImg() {
		return tpImg;
	}

	public void setTpImg(byte[] tpImg) {
		this.tpImg = tpImg;
	}

	public ArrayList<LocationPoint> getLocationPoints() {
		return locationPoints;
	}

	public void setLocationPoints(ArrayList<LocationPoint> locationPoints) {
		this.locationPoints = locationPoints;
	}
}
