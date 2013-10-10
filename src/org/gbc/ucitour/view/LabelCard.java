package org.gbc.ucitour.view;

import static org.gbc.ucitour.view.ViewHelper.dpInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class LabelCard extends Card {
  private String text;
  private TextView view;
  
  public LabelCard(Context context, String text) {
    super(context);
    setGravity(Gravity.CENTER_HORIZONTAL);
    setPadding(dpInt(25, context), dpInt(25, context), dpInt(25, context), dpInt(25, context));
    
    this.text = text;
    view = ViewHelper.text(context, text, 10);
    view.setLayoutParams(ViewHelper.layout(ViewHelper.WRAP_CONTENT, ViewHelper.WRAP_CONTENT));
    view.setTextColor(0xFF202020);
    addView(view);
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
    view.setText(text);
  }
}
