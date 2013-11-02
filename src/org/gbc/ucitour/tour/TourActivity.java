package org.gbc.ucitour.tour;

import java.util.List;

import org.gbc.ucitour.R;
import org.gbc.ucitour.model.TourPointInfo;
import org.gbc.ucitour.net.Query;
import org.gbc.ucitour.search.SearchableActivity;
import org.gbc.ucitour.view.ActionCard;
import org.gbc.ucitour.view.TourActionCard;
import org.gbc.ucitour.view.ViewHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class TourActivity extends SearchableActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater li = LayoutInflater.from(this);
		LinearLayout ll = (LinearLayout) li.inflate(R.layout.tour_view_select, contentContainer);

		// we should probably throw this query into a thread if it takes too
		// long...
		List<TourPointInfo> tours = Query.queryTours();

		ScrollView svLeft = (ScrollView) ll.findViewById(R.id.tour_main_scroll_left);
//		LinearLayout svLeftLL = new LinearLayout(this);
//		svLeftLL.setLayoutParams(ViewHelper.linearLayout(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT));
//		svLeftLL.setOrientation(LinearLayout.VERTICAL);
//		svLeftLL.addView(new ActionCard<TourMapFragmentsActivity>(this, TourMapFragmentsActivity.class, "Take a Tour", R.drawable.takeatour));
//		svLeft.addView(svLeftLL);

		ScrollView svRight = (ScrollView) ll.findViewById(R.id.tour_main_scroll_right);
		LinearLayout svRightLL = new LinearLayout(this);
		svRightLL.setLayoutParams(ViewHelper.linearLayout(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT));
		svRightLL.setOrientation(LinearLayout.VERTICAL);
		
		// just use takeatour picture since we dont have an image yet ...
		for (TourPointInfo e : tours) {
			svRightLL.addView(new TourActionCard<TourMapFragmentsActivity>(this, TourMapFragmentsActivity.class, e.getTpName(),
					R.drawable.takeatour, e.getTourId()));
		}
		svRight.addView(svRightLL);

	}
}
