package org.gbc.ucitour.view;

import static org.gbc.ucitour.view.ViewHelper.dpInt;

import org.gbc.ucitour.R;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

class Card extends LinearLayout {
  public Card(Context context) {
    super(context);
    LinearLayout.LayoutParams params = ViewHelper.linearLayout(ViewHelper.MATCH_PARENT,
        ViewHelper.WRAP_CONTENT);
    params.setMargins(dpInt(15, context), dpInt(18, context), dpInt(15, context), dpInt(0, context));
    setLayoutParams(params);
    init(context);
  }
  
  public Card(Context context, ViewGroup.LayoutParams params) {
    super(context);
    setLayoutParams(params);
    init(context);
  }

  private void init(Context context) {
    setBackgroundResource(R.drawable.content);
    setOrientation(LinearLayout.HORIZONTAL);
    setPadding(dpInt(15, context), dpInt(15, context), dpInt(15, context), dpInt(15, context));
  }
}
