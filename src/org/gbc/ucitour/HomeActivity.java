package org.gbc.ucitour;

import org.gbc.ucitour.tour.TourActivity;
import org.gbc.ucitour.history.HistoryActivity;
import org.gbc.ucitour.search.SearchableActivity;
import org.gbc.ucitour.view.ActionCard;

import android.os.Bundle;

/**
 * Main activity that is displayed when app starts
 */
public class HomeActivity extends SearchableActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Add action cards for various activities
    contentContainer.addView(new ActionCard<TourActivity>(this, TourActivity.class,
        "Take a Tour", R.drawable.takeatour));
    contentContainer.addView(new ActionCard<TourActivity>(this, TourActivity.class,
        "What's near me", R.drawable.whatsnear)); 
    contentContainer.addView(new ActionCard<HistoryActivity>(this, HistoryActivity.class,
        "History", R.drawable.history)); 
  }
}
