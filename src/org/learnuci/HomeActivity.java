package org.learnuci;

import org.learnuci.history.HistoryActivity;
import org.learnuci.search.SearchableActivity;
import org.learnuci.tour.TourActivity;
import org.learnuci.view.ActionCard;
import org.learnuci.view.ViewHelper;

import android.os.Bundle;

/**
 * Main activity that is displayed when app starts
 */
public class HomeActivity extends SearchableActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    contentContainer.setPadding(0, 0, 0, ViewHelper.dpInt(16, this));
    // Add action cards for various activities
    contentContainer.addView(new ActionCard<TourActivity>(this, TourActivity.class,
        "Take a Tour", R.drawable.takeatour));
    contentContainer.addView(new ActionCard<ProximityActivity>(this, ProximityActivity.class,
        "What's near me", R.drawable.whatsnear)); 
    contentContainer.addView(new ActionCard<HistoryActivity>(this, HistoryActivity.class,
        "History", R.drawable.history)); 
  }
}
