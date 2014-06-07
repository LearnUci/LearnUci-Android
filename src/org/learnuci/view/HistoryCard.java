package org.learnuci.view;

import org.learnuci.model.History;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.AbsListView;

@SuppressLint("ViewConstructor")
/**
 * Card that simply contains text and maps to a history element
 */
public class HistoryCard extends LabelCard {
  private History history;
  
  public HistoryCard(Context context, History history) {
    super(context, history.toString(), new AbsListView.LayoutParams(
        AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
    this.history = history;
  }
  
  public History getHistory() {
    return history;
  }
}
