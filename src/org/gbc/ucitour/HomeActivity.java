package org.gbc.ucitour;

import org.gbc.ucitour.ar.AugmentedRealityActivity;
import org.gbc.ucitour.search.SearchableActivity;
import org.gbc.ucitour.view.ActionCard;

import android.os.Bundle;

public class HomeActivity extends SearchableActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    contentContainer.addView(new ActionCard<AugmentedRealityActivity>(
        this, AugmentedRealityActivity.class, "Tours", R.drawable.graphic1));
    contentContainer.addView(new ActionCard<AugmentedRealityActivity>(
        this, AugmentedRealityActivity.class, "World View", R.drawable.graphic2));
    contentContainer.addView(new ActionCard<AugmentedRealityActivity>(
        this, AugmentedRealityActivity.class, "History"));
  }
}
