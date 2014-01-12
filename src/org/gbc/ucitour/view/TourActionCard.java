package org.gbc.ucitour.view;

import org.gbc.ucitour.history.PersistentHistory;
import org.gbc.ucitour.tour.TourMapFragmentsActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

@SuppressLint("ViewConstructor")
public class TourActionCard extends ActionCard<TourMapFragmentsActivity> implements OnClickListener{
	private long tourId;
	private Activity parent;
	private String tourName;
	
	public TourActionCard(Activity context, String text, Integer resId, long tourId) {
		super(context, TourMapFragmentsActivity.class, text, resId);
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
