package org.learnuci.view;

import static org.learnuci.view.ViewHelper.dpInt;

import org.learnuci.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A card that starts another activity when clicked
 * @param <T> A class that extends an Activity
 */
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
		setPadding(0, 0, 2, 2);
		setOrientation(VERTICAL);
		this.parent = context;
		this.clazz = clazz;
		setOnClickListener(this);

		RelativeLayout pictureWithLabel = new RelativeLayout(context);
		pictureWithLabel.setLayoutParams(ViewHelper.linearLayout(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT));
    RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT);
    ImageView iv = new ImageView(context);
    iv.setAdjustViewBounds(true);
    iv.setLayoutParams(ivParams);
		if (resId != null) {
			iv.setImageResource(resId);
			iv.setBackgroundColor(0xFFFFFFFF);
		} else {
      iv.setBackgroundColor(0xFF555555);
      iv.setLayoutParams(ivParams);
      iv.setMinimumHeight(ViewHelper.dpInt(100, context));
		}
    pictureWithLabel.addView(iv);
		TextView tv = ViewHelper.text(context, text, 16);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);

		tv.setTypeface(null, Typeface.BOLD);
		tv.setTextColor(0xFFFFFFFF);
		tv.setBackgroundColor(0x77000000);
		tv.setPadding(0, dpInt(8, context), 0, dpInt(8, context));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		tv.setLayoutParams(params);
		pictureWithLabel.addView(tv);
		
		addView(pictureWithLabel);
	}

	@Override
	public void onClick(View v) {
		parent.startActivity(new Intent(parent, clazz));
	}
}
