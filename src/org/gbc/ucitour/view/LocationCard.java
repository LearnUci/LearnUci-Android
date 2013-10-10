package org.gbc.ucitour.view;

import static org.gbc.ucitour.view.ViewHelper.dpInt;

import org.gbc.ucitour.model.LocationPoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public class LocationCard extends Card {
  public LocationCard(Context context, LocationPoint point) {
    super(context);
    if (point.hasImage()) {
      byte[] data = point.getImage();
      BitmapDrawable drawable = new BitmapDrawable(getResources(),
          BitmapFactory.decodeByteArray(data, 0, data.length));
      ImageView image = new ImageView(context);
      image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      image.setMaxHeight(dpInt(300, context));
      image.setMaxWidth(dpInt(400, context));
      image.setImageDrawable(drawable);
      addView(image);
    }
    
    LinearLayout info = new LinearLayout(context);
    info.setPadding(dpInt(5, context), 0, dpInt(5, context), 0);
    info.setLayoutParams(ViewHelper.layout(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT));
    info.setOrientation(VERTICAL);
    info.addView(ViewHelper.text(context, point.getName(), 5));
    info.addView(ViewHelper.text(context, point.getAbbreviation(), 4));
    addView(info);
  }
}
