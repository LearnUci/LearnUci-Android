package org.gbc.ucitour.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class TourActionCard<T extends Activity> extends ActionCard<T> implements OnClickListener{
	private long tourId;
	private Activity parent;
	private Class<T> clazz;
	public TourActionCard(Activity context, Class<T> clazz, String text, Integer resId, long tourId) {
		super(context, clazz, text, resId);
		this.tourId = tourId;
		this.parent = context;
		this.clazz = clazz;
	}

	public long getTourId(){
		return this.tourId;
	}
	
	public void onClick(View v) {
		parent.startActivity(new Intent(parent, clazz).putExtra("tourId", tourId));
	}
}
