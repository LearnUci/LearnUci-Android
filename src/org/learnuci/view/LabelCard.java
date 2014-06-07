package org.learnuci.view;

import static org.learnuci.view.ViewHelper.dpInt;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class LabelCard extends Card {
  private String text;
  private TextView view;
  
  public LabelCard(Context context, String text) {
    super(context);
    setPadding(dpInt(25, context), dpInt(25, context), dpInt(25, context), dpInt(25, context));
    
    this.text = text;
    view = ViewHelper.text(context, text, 15);
    view.setLayoutParams(ViewHelper.layout(ViewHelper.WRAP_CONTENT, ViewHelper.WRAP_CONTENT));
    addView(view);
  }
  
  public LabelCard(Context context, String text, ViewGroup.LayoutParams params) {
    super(context, params);
    setPadding(dpInt(25, context), dpInt(25, context), dpInt(25, context), dpInt(25, context));
    
    this.text = text;
    view = ViewHelper.text(context, text, 15);
    view.setLayoutParams(ViewHelper.layout(ViewHelper.WRAP_CONTENT, ViewHelper.WRAP_CONTENT));
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
