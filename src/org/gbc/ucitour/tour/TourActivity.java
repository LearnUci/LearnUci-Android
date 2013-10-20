package org.gbc.ucitour.tour;

import org.gbc.ucitour.R;
import org.gbc.ucitour.ar.AugmentedRealityActivity;
import org.gbc.ucitour.search.SearchableActivity;
import org.gbc.ucitour.view.ActionCard;

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

		ScrollView svLeft = (ScrollView) ll.findViewById(R.id.tour_main_scroll_left);
		svLeft.addView(new ActionCard<AugmentedRealityActivity>(this, AugmentedRealityActivity.class, "Take a Tour", R.drawable.takeatour));

		ScrollView svRight = (ScrollView) ll.findViewById(R.id.tour_main_scroll_right);
		svRight.addView(new ActionCard<AugmentedRealityActivity>(this, AugmentedRealityActivity.class, "Take a Tour", R.drawable.takeatour));

	}
}
