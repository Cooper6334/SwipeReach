package com.example.swipereach;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NoteSurface extends SurfaceView {
	boolean flagIsDrawing = false;
	Paint p;
	SurfaceHolder holder;
	int preX = -1, preY = -1;
	int lr = 1;
	float px, py, sx, sy;
	float width, height;

	public NoteSurface(Context context, int w, int h, int inlr) {
		super(context);
		setBackgroundColor(0);

		width = w;
		height = h;
		lr = inlr;
		p = new Paint();
		p.setColor(Color.BLUE);
		p.setStrokeWidth(10);

		holder = getHolder();
		holder.setFormat(PixelFormat.TRANSLUCENT);
		// TODO Auto-generated constructor stub
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// Log.e("touch", "node touch" + event.getAction());
	//
	// return true;
	// }
	void drawAt(float x, float y) {
		if (lr == 0) {
			px = x * 2;
		} else {
			px = (x - sx) * 2;
		}
		py = (y - sy) * 2;
		if (px > width) {
			px = width;
		} else if (px < 0) {
			px = 0;
		}
		if (py > height) {
			py = height;
		} else if (py < 40) {
			py = 40;
		}
	}

	void draw() {
		Canvas c = holder.lockCanvas();
		if (c != null) {
			c.drawColor(Color.TRANSPARENT, Mode.CLEAR);
			// c.drawColor(Color.RED);
			c.drawLine(sx, sy, sx + width / 2, sy, p);

			c.drawLine(sx, sy + height / 2, sx + width / 2, sy + height / 2, p);
			c.drawLine(sx + width / 2, sy, sx + width / 2, sy + height / 2, p);
			c.drawLine(sx, sy, sx, sy + height / 2, p);
			c.drawCircle(px, py, 50, p);
			holder.unlockCanvasAndPost(c);
		}
	}

	void resetDraw() {
		preX = -1;
		preY = -1;
	}

	public void startDraw(float x, float y) {
		flagIsDrawing = true;
		if (lr == 0) {
			sx = 0;
		} else {
			sx = width / 2;
		}
		sy = y / 2;
		if (lr == 0) {
			sx += 50;
		} else {
			sx -= 50;
		}
		sy += 100;
		if (sx + width / 2 > width) {
			sx = width / 2;
		}
		if (sy + height / 2 > height) {
			sy = height / 2;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (flagIsDrawing) {
					draw();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void stopDraw() {
		flagIsDrawing = false;
		Canvas c = holder.lockCanvas();
		if (c != null) {
			c.drawColor(Color.TRANSPARENT, Mode.CLEAR);
			holder.unlockCanvasAndPost(c);
		}
	}
}
