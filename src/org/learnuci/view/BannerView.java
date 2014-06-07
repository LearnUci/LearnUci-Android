package org.learnuci.view;

import org.learnuci.R;
import org.learnuci.search.SearchableActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

public class BannerView extends RelativeLayout implements OnEditorActionListener, OnFocusChangeListener {
  private Context context;
  private SearchableActivity activity;
  private RelativeLayout container;
  private EditText input;
  private boolean searching = false;
  
  public BannerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }
  
  public BannerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }
  
  public BannerView(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    int ind = (int) ((System.currentTimeMillis() / (1000 * 60 * 60 * 24)) % 4);
    ImageView imageBg = new ImageView(context);
    switch (ind) {
      case 0:
        imageBg.setImageResource(R.drawable.banner1);
        break;
      case 1:
        imageBg.setImageResource(R.drawable.banner2);
        break;
      case 2:
        imageBg.setImageResource(R.drawable.banner3);
        break;
      case 3:
        imageBg.setImageResource(R.drawable.banner4);
        break;
    }
    imageBg.setLayoutParams(ViewHelper.layout(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT));
    imageBg.setAdjustViewBounds(true);
    addView(imageBg);
    addView(View.inflate(context, R.layout.banner_overlay, null));
    
    input = (EditText) findViewById(R.id.input);
    input.setOnFocusChangeListener(this);
    input.setOnEditorActionListener(this);
    
    container = (RelativeLayout) findViewById(R.id.banner_overlay);
    container.setFocusable(true);
    container.setFocusableInTouchMode(true);
    
    findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        activity.setQuery(input.getText().toString());
      }
    });
  }
  
  public boolean back() {
    if (searching) {
      input.setText("");
      activity.clearQuery();
      container.requestFocus();
      searching = false;
      activity.setShowSearch(false);
      return false;
    }
    return true;
  }
  
  public void setText(String text) {
    input.setText(text); 
  }
  
  public void setIsSearching(boolean searching) {
    this.searching = searching;
  }
  
  public void setOnSearchListener(SearchableActivity activity) {
    this.activity = activity;
  }
  
  @Override
  public void onFocusChange(View view, boolean hasFocus) {
    searching = hasFocus;
    activity.setShowSearch(searching);
  }

  @Override
  public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_DONE) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
      activity.setQuery(input.getText().toString());
    }
    return true;
  }
}
