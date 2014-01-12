package org.gbc.ucitour.model;

import java.util.ArrayList;

import android.util.Base64;

import com.google.gson.JsonObject;

public class TourPointInfo {
	//name of place
	private String tpName;
	//description
	private String tpDesc;
	//image
	private byte[] tpImg;
	//list of location point IDs to query if selected
	private ArrayList<Long> locIds;
	//this tour's ID
	private long id;
	
	//list of location points
	private ArrayList<LocationPoint> locationPoints;
	
	public TourPointInfo(JsonObject tpInfo){
		tpName = tpInfo.get("name").getAsString();
		tpDesc = tpInfo.get("desc").getAsString();
	    if (tpInfo.has("img") && !tpInfo.get("img").getAsString().equals("null")) {
	    	tpImg = Base64.decode(tpInfo.get("img").getAsString(), Base64.DEFAULT);
	    }
	    locIds = new ArrayList<Long>();
	    String lps = tpInfo.get("points").getAsString();
	    //trim off brackets
	    lps = lps.substring(1, lps.length() - 2);
	    //get list of location point IDs
	    String[] tempLocIds = lps.split("\\,");
	    for(String e : tempLocIds){
	    	locIds.add(Long.parseLong(e));
	    }
	    id = tpInfo.get("id").getAsLong();
	    
	    locationPoints = new ArrayList<LocationPoint>();
//	    for(JsonElement e : lps){
//	    	locationPoints.add(new LocationPoint(e.getAsJsonObject()));
//	    }
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

	public long getTourId() {
		return this.id;
	}
}
