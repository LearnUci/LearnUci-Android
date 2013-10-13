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

/**
 * Activity that contains a banner with a search bar.
 * Handles searching.
 */
public class SearchableActivity extends Activity {
  protected LinearLayout contentContainer;
  protected LinearLayout resultContainer;
  private BannerView banner;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    // Content container contains the content for the page
    contentContainer = (LinearLayout) findViewById(R.id.content);
    
    // Result container contains the search results
    resultContainer = (LinearLayout) findViewById(R.id.search_results);
    
    // Banner view
    banner = ((BannerView) findViewById(R.id.banner));
    banner.setOnSearchListener(this);
    
    // Don't show search to begin with
    setShowSearch(false);
  }
  
  @Override
  public void onBackPressed() {
    // If in search mode, exit search mode, else, do default back button action
    if (banner.back()) {
      super.onBackPressed();
    }
  }
  
  public void setShowSearch(boolean show) {
    // Display only one container depending on the mode
    contentContainer.setVisibility(show ? LinearLayout.GONE : LinearLayout.VISIBLE);
    resultContainer.setVisibility(show ? LinearLayout.VISIBLE : LinearLayout.GONE);
  }
  
  public void setQuery(String query) {
    // Make a query based on the keywords
    List<LocationPoint> results = Query.query("SEARCH", "KEYWORD", query);
    clearQuery();
    
    // Populate the results with the points
    for (LocationPoint point : results) {
      resultContainer.addView(new LocationCard(this, point));
    }
  }
  
  public void clearQuery() {
    resultContainer.removeAllViews();
  }                   
}
