package com.example.swipereach;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class WindowService extends IntentService {
	boolean flagIsRunning = false;
	SharedPreferences preference;

	boolean flag = true;
	PieView lView, rView;

	public WindowService() {
		super("WindowService");
	}

	public WindowService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.e("service", "oncreate");
		super.onCreate();
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		preference = getSharedPreferences("iSwipe", 0);
		int w = preference.getInt("barWidth", 30);
		if (w > 45) {
			w = 45;
		} else if (w < 15) {
			w = 15;
		}
		if (preference.getInt("runningLeft", 0) == 1) {
			lView = new PieView(this, wm, preference.getInt("width", 480),
					preference.getInt("height", 800), w, 0);
		}
		if (preference.getInt("runningRight", 0) == 1) {
			rView = new PieView(this, wm, preference.getInt("width", 480),
					preference.getInt("height", 800), w, 1);
		}

	}

	@Override
	public void onStart(Intent intent, int id) {
		Log.e("myservice", "start");
		// Toast.makeText(this, "Start Service", Toast.LENGTH_LONG).show();

		if (intent != null && intent.getBooleanExtra("testMode", false)) {
			if (lView != null) {
				lView.setTestMode();
			}
			if (rView != null) {
				rView.setTestMode();
			}
		}

		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				MainActivity.class), 0);
		Notification n = new Notification();
		n.icon = R.drawable.ic_launcher;
		n.tickerText = "iSwipe";
		n.setLatestEventInfo(this, "iSwipe", "Touch to setting", pi);
		this.startForeground(6334, n);

	}

	@Override
	public boolean stopService(Intent intent) {
		Log.e("myservice", "stop service");
		this.stopSelf();
		return true;
	}

	public void test(float x, float y) {
		Toast.makeText(this, (int) x + "," + (int) y, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroy() {
		Log.e("myservice", "destroy");
		// Toast.makeText(this, "Stop Service", Toast.LENGTH_LONG).show();
		flagIsRunning = false;
		flag = false;
		super.onDestroy();

		if (lView != null) {
			((WindowManager) getSystemService(WINDOW_SERVICE))
					.removeView(lView);

			lView = null;
		}
		if (rView != null) {
			((WindowManager) getSystemService(WINDOW_SERVICE))
					.removeView(rView);

			rView = null;
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("myservice", "start Command");
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

}
