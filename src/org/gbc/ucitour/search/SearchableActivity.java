package org.gbc.ucitour.search;

import java.util.List;

import org.gbc.ucitour.R;
import org.gbc.ucitour.history.PersistentHistory;
import org.gbc.ucitour.model.LocationPoint;
import org.gbc.ucitour.net.Query;
import org.gbc.ucitour.tour.TourMapFragmentsActivity;
import org.gbc.ucitour.view.BannerView;
import org.gbc.ucitour.view.LabelCard;
import org.gbc.ucitour.view.LocationCard;
import org.gbc.ucitour.view.ViewHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Activity that contains a banner with a search bar.
 * Handles searching.
 */
public class SearchableActivity extends Activity implements OnClickListener {
  protected LinearLayout contentContainer;
  protected LinearLayout resultContainer;
  protected LinearLayout nonScrollableContainer;
  protected RelativeLayout homeContainer;
  
  protected BannerView banner;
  
  private boolean isShown;
  private Object setShownLock = new Object();
  
  private boolean searchLoading = false;
  private boolean contentLoading = false;
  private Object loadingChangeLock = new Object();

  private ProgressBar progressBar;
  
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
    
    // Home container
    homeContainer = (RelativeLayout) findViewById(R.id.home_container);
    progressBar = new ProgressBar(this);
    RelativeLayout.LayoutParams params =
        ViewHelper.layout(ViewHelper.WRAP_CONTENT, ViewHelper.WRAP_CONTENT);
    params.addRule(RelativeLayout.CENTER_IN_PARENT);
    progressBar.setLayoutParams(params);
    
    // Banner view
    banner = ((BannerView) findViewById(R.id.banner));
    banner.setOnSearchListener(this);
    
    // Don't show search to begin with
    setShowSearch(false);
  }
  
  protected void setSearchLoading(boolean searchLoading) {
    synchronized (loadingChangeLock) {
      this.searchLoading = searchLoading;
    }
  }
  
  protected void setContentLoading(boolean contentLoading) {
    synchronized (loadingChangeLock) {
      this.contentLoading = contentLoading;
    }
  }
  
  protected boolean isLoading() {
    synchronized (loadingChangeLock) {
      return this.contentLoading || this.searchLoading;
    }
  }
  
  
  @Override
  public void onBackPressed() {
    boolean overrideBack = banner.back();
    // If in search mode, exit search mode, else, do default back button action
    for (int i = 0; i < homeContainer.getChildCount(); i++) {
      if (homeContainer.getChildAt(i) instanceof ProgressBar) {
        setSearchLoading(false);
        if (!isLoading()) {
          homeContainer.removeViewAt(i);
        }
        break;
      }
    }
    if (overrideBack) {
      super.onBackPressed();
    }
  }

  public void setShowSearch(boolean show) {
    synchronized (setShownLock) {
      // Display only one container depending on the mode
      contentContainer.setVisibility(show ? LinearLayout.GONE : LinearLayout.VISIBLE);
      nonScrollableContainer.setVisibility(show ? LinearLayout.GONE : LinearLayout.VISIBLE);
      resultContainer.setVisibility(show ? LinearLayout.VISIBLE : LinearLayout.GONE);
      isShown = show;
    }
  }
  
  public void setQuery(final String query) {
    PersistentHistory.getInstance().addHistory(PersistentHistory.TYPE_SEARCH, query, "",
        String.valueOf(System.currentTimeMillis()));
    if (!isLoading()) {
      homeContainer.addView(progressBar);
    }
    setSearchLoading(true);
    new Thread(new Runnable() {
      @Override
      public void run() {
        // Make a query based on the keywords
        final List<LocationPoint> results = Query.query("SEARCH", "KEYWORD", query);
        SearchableActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            clearQuery();
          }
        });
        
        synchronized (setShownLock) {
          if (isShown) {
            // Populate the results with the points
            SearchableActivity.this.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                setSearchLoading(false);
                if (!isLoading()) {
                  homeContainer.removeView(progressBar);
                }
                if (results.isEmpty()) {
                  LabelCard label = new LabelCard(SearchableActivity.this, "No Results Found.");
                  resultContainer.addView(label);
                  return;
                }
                for (LocationPoint point : results) {
                  LocationCard card = new LocationCard(SearchableActivity.this, point);
                  card.setOnClickListener(SearchableActivity.this);
                  resultContainer.addView(card);
                }
              }
            });
          }
        }
      }
    }).start();
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
