package org.gbc.ucitour.search;

import java.util.List;

import org.gbc.ucitour.R;
import org.gbc.ucitour.model.LocationPoint;
import org.gbc.ucitour.net.Query;
import org.gbc.ucitour.view.BannerView;
import org.gbc.ucitour.view.LocationCard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class SearchableActivity extends Activity {
  protected LinearLayout contentContainer;
  protected LinearLayout resultContainer;
  private BannerView banner;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    contentContainer = (LinearLayout) findViewById(R.id.content);
    resultContainer = (LinearLayout) findViewById(R.id.search_results);
    banner = ((BannerView) findViewById(R.id.banner));
    banner.setOnSearchListener(this);
    setShowSearch(false);
  }
  
  @Override
  public void onBackPressed() {
    if (banner.back()) {
      super.onBackPressed();
    }
  }
  
  public void setShowSearch(boolean show) {
    contentContainer.setVisibility(show ? LinearLayout.GONE : LinearLayout.VISIBLE);
    resultContainer.setVisibility(show ? LinearLayout.VISIBLE : LinearLayout.GONE);
  }
  
  public void setQuery(String query) {
    List<LocationPoint> results = Query.query("SEARCH", "KEYWORD", query);
    clearQuery();
    for (LocationPoint point : results) {
      resultContainer.addView(new LocationCard(this, point));
    }
  }
  
  public void clearQuery() {
    resultContainer.removeAllViews();
  }                   
}
