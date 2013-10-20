package org.gbc.ucitour.view;

import static org.gbc.ucitour.view.ViewHelper.dpInt;

import org.gbc.ucitour.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		if (resId != null) {
			RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(ViewHelper.MATCH_PARENT, ViewHelper.WRAP_CONTENT);
			ImageView iv = new ImageView(context);
			iv.setImageResource(resId);
			iv.setLayoutParams(ivParams);
			iv.setAdjustViewBounds(true);
			iv.setBackgroundColor(0xFFFFFFFF);
			pictureWithLabel.addView(iv);
		}
		
		TextView tv = ViewHelper.text(context, text, 10);
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
