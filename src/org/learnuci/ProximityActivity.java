package org.learnuci;

import java.util.List;

import org.learnuci.location.LocationProvider;
import org.learnuci.model.LocationPoint;
import org.learnuci.net.Query;
import org.learnuci.search.SearchableActivity;
import org.learnuci.view.LabelCard;
import org.learnuci.view.LocationCard;
import org.learnuci.view.ViewHelper;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ProximityActivity extends SearchableActivity {
  private ProgressBar progressBar;
  private LabelCard locationNotFound;
  
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    progressBar = new ProgressBar(this);
    RelativeLayout.LayoutParams params = ViewHelper.layout(ViewHelper.WRAP_CONTENT, ViewHelper.WRAP_CONTENT);
    params.addRule(RelativeLayout.CENTER_IN_PARENT);
    progressBar.setLayoutParams(params);
    
    locationNotFound = new LabelCard(this, "No Location Found, Try Again");
    locationNotFound.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        tryGetLocation();
      }
    });
    
    tryGetLocation();
  }
  
  private void tryGetLocation() {
    contentContainer.removeAllViews();
    
    if (!isLoading()) {
      homeContainer.addView(progressBar);
    }
    setContentLoading(true);
    
    final Location loc = LocationProvider.instance(ProximityActivity.this).getLocation();
    if (loc == null) {
      setContentLoading(false);
      if (!isLoading()) {
        homeContainer.removeView(progressBar);
      }
      contentContainer.addView(locationNotFound);
      return;
    }
    
    new Thread(new Runnable() {
      @Override
      public void run() {
        final List<LocationPoint> tours = Query.query("PROXIMITY", "", "{\"latitude\":"
            + loc.getLatitude() + ",\"longitude\":" + loc.getLongitude() + "}");
        ProximityActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            setContentLoading(false);
            if (!isLoading()) {
              homeContainer.removeView(progressBar);
            }
            for (LocationPoint point : tours) {
              LocationCard locationCard = new LocationCard(ProximityActivity.this, point);
              locationCard.setOnClickListener(ProximityActivity.this);
              contentContainer.addView(locationCard);
            }
          }
        });
      }
    }).start();
  }
}
