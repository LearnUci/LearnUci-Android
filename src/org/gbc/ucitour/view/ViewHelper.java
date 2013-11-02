package org.gbc.ucitour.view;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewHelper {
  public static int MATCH_PARENT = RelativeLayout.LayoutParams.MATCH_PARENT;
  public static int WRAP_CONTENT = RelativeLayout.LayoutParams.WRAP_CONTENT;
  
  private ViewHelper() { }
  
  public static float dp(float value, Context context) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
        context.getResources().getDisplayMetrics()); 
  }
  
  public static int dpInt(float value, Context context) {
    return (int) dp(value, context);
  }
  
  public static float sp(float value, Context context) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
        context.getResources().getDisplayMetrics());
  }
  
  public static int spInt(float value, Context context) {
    return (int) sp(value, context);
  }
  
  public static RelativeLayout.LayoutParams layout(int w, int h) {
    return new RelativeLayout.LayoutParams(w, h);
  }
  
  public static LinearLayout.LayoutParams linearLayout(int w, int h) {
    return new LinearLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT == w ?
            LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.MATCH_PARENT == w ?
            LinearLayout.LayoutParams.MATCH_PARENT : LinearLayout.LayoutParams.WRAP_CONTENT);
  }
  
  public static TextView text(Context context, String value, int spSize) {
    TextView textView = new TextView(context);
    textView.setText(value);
    textView.setTextSize(spInt(spSize, context));
    return textView;
  }
}
