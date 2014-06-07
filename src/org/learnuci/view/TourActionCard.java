package org.learnuci.view;

import org.learnuci.history.PersistentHistory;
import org.learnuci.tour.TourMapFragmentsActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;

@SuppressLint("ViewConstructor")
public class TourActionCard extends ActionCard<TourMapFragmentsActivity> implements OnClickListener{
	private long tourId;
	private Activity parent;
	private String tourName;
	
	// Targeting older devices, so I'm preferring to use deprecated rather than only support api >= 16
	@SuppressWarnings("deprecation")
  public TourActionCard(Activity context, String text, Bitmap bmp, long tourId) {
		super(context, TourMapFragmentsActivity.class, text);
		if (bmp != null) {
		  setBackgroundDrawable(new BitmapDrawable(getResources(), bmp));
		}
		this.tourId = tourId;
		this.parent = context;
		this.tourName = text;
	}

	public long getTourId(){
		return this.tourId;
	}
	
	public void onClick(View v) {
	  PersistentHistory.getInstance().addHistory(PersistentHistory.TYPE_TOUR, tourName,
	      String.valueOf(tourId), String.valueOf(System.currentTimeMillis()));
		parent.startActivity(new Intent(parent, TourMapFragmentsActivity.class).putExtra("tourId", tourId));
	}
}
