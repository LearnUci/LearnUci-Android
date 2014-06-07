package org.learnuci.tour;

import java.util.List;

import org.learnuci.R;
import org.learnuci.model.TourPointInfo;
import org.learnuci.net.Query;
import org.learnuci.search.SearchableActivity;
import org.learnuci.view.TourActionCard;
import org.learnuci.view.ViewHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class TourActivity extends SearchableActivity {
  private ProgressBar progressBar;
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressBar = new ProgressBar(this);
		RelativeLayout.LayoutParams params = ViewHelper.layout(ViewHelper.WRAP_CONTENT, ViewHelper.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		progressBar.setLayoutParams(params);
		
		if (!isLoading()) {
		  homeContainer.addView(progressBar);
		}
    setContentLoading(true);
		
		new Thread(new Runnable() {
		  @Override
		  public void run() {
		    final List<TourPointInfo> tours = Query.queryTours();
		    TourActivity.this.runOnUiThread(new Runnable() {
		      @Override
		      public void run() {
		        setContentLoading(false);
		        if (!isLoading()) {
		          homeContainer.removeView(progressBar);
		        }
		        LayoutInflater li = LayoutInflater.from(TourActivity.this);
		        LinearLayout ll = (LinearLayout) li.inflate(R.layout.tour_view_select, contentContainer);

		        ScrollView svRight = (ScrollView) ll.findViewById(R.id.tour_main_scroll_right);
		        LinearLayout svRightLL = new LinearLayout(TourActivity.this);
		        svRightLL.setLayoutParams(ViewHelper.linearLayout(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT));
		        svRightLL.setOrientation(LinearLayout.VERTICAL);
		        
		        // just use takeatour picture since we dont have an image yet ...
		        for (TourPointInfo e : tours) {
		          byte[] data = e.getTpImg();
		          Bitmap img = null;
		          if (data != null) {
		            img = BitmapFactory.decodeByteArray(data, 0, data.length);
		          }
		          svRightLL.addView(new TourActionCard(TourActivity.this, e.getTpName(), img,
		              e.getTourId()));
		        }
		        svRight.addView(svRightLL);
		      }
		    });
		  }}).start();
	}
}
