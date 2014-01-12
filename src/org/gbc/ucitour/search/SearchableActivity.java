package org.gbc.ucitour.search;

import java.util.List;

import org.gbc.ucitour.R;
import org.gbc.ucitour.history.PersistentHistory;
import org.gbc.ucitour.model.LocationPoint;
import org.gbc.ucitour.net.Query;
import org.gbc.ucitour.tour.TourMapFragmentsActivity;
import org.gbc.ucitour.view.BannerView;
import org.gbc.ucitour.view.LocationCard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * Activity that contains a banner with a search bar.
 * Handles searching.
 */
public class SearchableActivity extends Activity implements OnClickListener {
  protected LinearLayout contentContainer;
  protected LinearLayout resultContainer;
  protected LinearLayout nonScrollableContainer;
  
  protected BannerView banner;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    // Content container contains the content for the page
    contentContainer = (LinearLayout) findViewById(R.id.content);
    
    // Result container contains the search results
    resultContainer = (LinearLayout) findViewById(R.id.search_results);
    
    // Content container that doesn't scroll (match parent height)
    nonScrollableContainer = (LinearLayout) findViewById(R.id.nonscrollcontent);
    
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
    nonScrollableContainer.setVisibility(show ? LinearLayout.GONE : LinearLayout.VISIBLE);
    resultContainer.setVisibility(show ? LinearLayout.VISIBLE : LinearLayout.GONE);
  }
  
  public void setQuery(String query) {
    PersistentHistory.getInstance().addHistory(PersistentHistory.TYPE_SEARCH, query, "",
        String.valueOf(System.currentTimeMillis()));
    
    // Make a query based on the keywords
    List<LocationPoint> results = Query.query("SEARCH", "KEYWORD", query);
    clearQuery();
    
    // Populate the results with the points
    for (LocationPoint point : results) {
      LocationCard card = new LocationCard(this, point);
      card.setOnClickListener(this);
      resultContainer.addView(card);
    }
  }
  
  public void clearQuery() {
    resultContainer.removeAllViews();
  }

  @Override
  public void onClick(View v) {
    if (v instanceof LocationCard) {
      LocationPoint point = ((LocationCard) v).getPoint();
      Intent intent = new Intent(this, TourMapFragmentsActivity.class);
      intent.putExtra("locationId", point.getId());
      startActivity(intent);
    }
  }                   
}
