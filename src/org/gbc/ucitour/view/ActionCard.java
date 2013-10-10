package org.gbc.ucitour.view;

import static org.gbc.ucitour.view.ViewHelper.dpInt;

import org.gbc.ucitour.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class ActionCard<T extends Activity> extends Card implements OnClickListener {
  private Class<T> clazz;
  private Activity parent;
  
  public ActionCard(Activity context, Class<T> clazz, String text) {
    this(context, clazz, text, null);
  }
  
  public ActionCard(Activity context, Class<T> clazz, String text, Integer resId) {
    super(context);
    setBackgroundResource(R.drawable.content);
    setPadding(1, dpInt(10, context), 1, dpInt(15, context));
    setOrientation(VERTICAL);
    this.parent = context;
    this.clazz = clazz;
    setOnClickListener(this);
    TextView tv = ViewHelper.text(context, text, 10);
    tv.setTextColor(0xFF303030);
    LinearLayout.LayoutParams params = ViewHelper.linearLayout(ViewHelper.WRAP_CONTENT, ViewHelper.WRAP_CONTENT);
    params.setMargins(dpInt(15, context), 0, 0, 0);
    tv.setLayoutParams(params);
    addView(tv);
    
    if (resId != null) {
      LinearLayout.LayoutParams ivParams = ViewHelper.linearLayout(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT);
      ivParams.setMargins(0, dpInt(10, context), 0, 0);
      ImageView iv = new ImageView(context);
      iv.setImageResource(resId);
      iv.setLayoutParams(ivParams);
      iv.setAdjustViewBounds(true);
      iv.setPadding(0, 1, 0, 1);
      iv.setBackgroundColor(0xFF303030);
      addView(iv);
    }
  }

  @Override
  public void onClick(View v) {
    parent.startActivity(new Intent(parent, clazz));
  }
}
