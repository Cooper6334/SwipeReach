package com.example.swipereach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

class PieView extends RelativeLayout {
	// Button sur;
	float tx = 0, ty = 0;
	WindowManager wm;
	WindowManager.LayoutParams viewParams;// , outParams;
	NoteSurface note;
	int w = 30;
	int width, height;
	int lr = 0;// 0左1右

	public PieView(Context context, WindowManager w, int wid, int hei, int inw,
			int inlr) {
		super(context, null, 0);
		Log.e("myservice", "swipeview create");
		wm = w;
		width = wid;
		height = hei;
		lr = inlr;
		this.w = inw;
		init();
	}

	public void setTestMode() {
		// if (lr == 0) {
		// this.setBackgroundColor(0x55ff0000);
		// } else {
		// this.setBackgroundColor(0x5500ff00);
		// }
	}

	void init() {
		this.setBackgroundColor(0);
		viewParams = new WindowManager.LayoutParams(
				// WindowManager.LayoutParams.MATCH_PARENT,
				// WindowManager.LayoutParams.MATCH_PARENT,
				w, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,

				PixelFormat.TRANSLUCENT);
		viewParams.gravity = Gravity.LEFT;
		if (lr == 0) {
			// viewParams.gravity = Gravity.LEFT;
			viewParams.x = 0;
		} else {
			// viewParams.gravity = Gravity.RIGHT;
			viewParams.x = width - w;
		}
		note = new NoteSurface(getContext(), width, height, lr);
		this.addView(note, new RelativeLayout.LayoutParams(width, height));
		wm.addView(this, viewParams);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// return super.onTouchEvent(event);
		Log.e("touch", "view touch");
		float x = event.getX() - tx;
		float y = event.getY() - ty;
		switch (event.getAction()) {
		// case MotionEvent.ACTION_OUTSIDE:
		// Log.e("touch", "outside");
		//
		// break;
		case MotionEvent.ACTION_DOWN:

			note.startDraw(x, y);
			viewParams.width = width;
			wm.updateViewLayout(this, viewParams);
			break;
		case MotionEvent.ACTION_MOVE:
			if (note.flagIsDrawing) {
				note.drawAt(x, y);
			}
			break;
		case MotionEvent.ACTION_UP:
			// Log.e("touch", "up");
			note.stopDraw();
			viewParams.width = w;
			wm.updateViewLayout(this, viewParams);

			((WindowService) getContext()).test(note.px, note.py);

		}

		return true;
	}

}
