package org.gbc.ucitour.history;

import org.gbc.ucitour.search.SearchableActivity;
import org.gbc.ucitour.tour.TourMapFragmentsActivity;
import org.gbc.ucitour.view.HistoryCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HistoryActivity extends SearchableActivity implements OnItemClickListener {
  // ListView to hold all the history items
  private ListView listView;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    listView = new ListView(this);
    super.onCreate(savedInstanceState);
    listView.setAdapter(new HistoryAdapter(this, PersistentHistory.getInstance().listHistory()));
    
    // Listen for when an item is clicked in the listview
    listView.setOnItemClickListener(this);
    
    // Insert listView into the nonscrollabe container so the listview itself can take up the whole
    // page
    nonScrollableContainer.addView(listView);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    // Cast to HistoryCard
    HistoryCard history = (HistoryCard) view;
    
    // Split it into its type and keywords parts
    String type = history.getHistory().type;
    String keywords = history.getHistory().keyword;
    String historyId = history.getHistory().id;
    
    // IF the type is to search, then search for the given keywords
    if (type.equals(PersistentHistory.TYPE_SEARCH)) {
      setShowSearch(true);
      banner.setText(keywords);
      banner.setIsSearching(true);
      setQuery(keywords);
      
    // If the type is to take a tour, then start the tour with this activity as the parent
    } else if (type.equals(PersistentHistory.TYPE_TOUR)) {
      PersistentHistory.getInstance().addHistory(PersistentHistory.TYPE_TOUR, keywords,
          historyId, String.valueOf(System.currentTimeMillis()));
      listView.setAdapter(new HistoryAdapter(this, PersistentHistory.getInstance().listHistory()));
      this.startActivity(new Intent(this, TourMapFragmentsActivity.class)
          .putExtra("tourId", Long.parseLong(historyId)));
    }
  }
  
  @Override
  public void setShowSearch(boolean show) {
    super.setShowSearch(show);
    // If exiting search mode from this activity, make sure to update the listview for the keywords
    // just recently searched
    if (!show) {
      listView.setAdapter(new HistoryAdapter(this, PersistentHistory.getInstance().listHistory()));
    }
  }
}
