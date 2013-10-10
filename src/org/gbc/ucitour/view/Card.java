package org.gbc.ucitour.view;

import static org.gbc.ucitour.view.ViewHelper.dpInt;

import org.gbc.ucitour.R;

import android.content.Context;
import android.widget.LinearLayout;

class Card extends LinearLayout {
  public Card(Context context) {
    super(context);
    init(context);
  }
  
  private void init(Context context) {
    LinearLayout.LayoutParams params = ViewHelper.linearLayout(ViewHelper.MATCH_PARENT,
        ViewHelper.WRAP_CONTENT);
    params.setMargins(dpInt(15, context), dpInt(18, context), dpInt(15, context), dpInt(18, context));
    setLayoutParams(params);
    setBackgroundResource(R.drawable.content);
    setOrientation(LinearLayout.HORIZONTAL);
    setPadding(dpInt(15, context), dpInt(15, context), dpInt(15, context), dpInt(15, context));
  }
}
