package org.learnuci.history;

import org.learnuci.model.History;
import org.learnuci.view.HistoryCard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Adapter to map history to history cards
 */
public class HistoryAdapter extends ArrayAdapter<History> {
  private Context context;
  private History[] history;
  
  public HistoryAdapter(Context context, History[] history) {
    super(context, android.R.layout.simple_list_item_1, history);
    this.context = context;
    this.history = history;
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return new HistoryCard(context, history[position]);
  }
}
