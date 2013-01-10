package com.example.swipereach;

import java.util.List;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MainActivity extends Activity {

	SharedPreferences preference;
	CheckBox c1, c2;
	SeekBar widthBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preference = getSharedPreferences("iSwipe", 0);

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		preference.edit().putInt("width", dm.widthPixels).commit();
		preference.edit().putInt("height", dm.heightPixels).commit();

		c1 = (CheckBox) findViewById(R.id.checkBox1);
		if (preference.getInt("runningRight", -1) == 1) {
			c1.setChecked(true);
		}
		c1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					preference.edit().putInt("runningRight", 1).commit();
					Intent i = new Intent(MainActivity.this,
							WindowService.class);
					stopService(i);
					i.putExtra("testMode", true);
					startService(i);
				} else {
					preference.edit().putInt("runningRight", 0).commit();
					Intent i = new Intent(MainActivity.this,
							WindowService.class);
					stopService(i);
					if (c2.isChecked()) {
						i.putExtra("testMode", true);
						startService(i);
					}

				}
			}
		});

		c2 = (CheckBox) findViewById(R.id.checkBox2);
		if (preference.getInt("runningLeft", -1) == 1) {
			c2.setChecked(true);
		}
		c2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					preference.edit().putInt("runningLeft", 1).commit();
					Intent i = new Intent(MainActivity.this,
							WindowService.class);
					stopService(i);
					i.putExtra("testMode", true);
					startService(i);
				} else {
					preference.edit().putInt("runningLeft", 0).commit();
					Intent i = new Intent(MainActivity.this,
							WindowService.class);
					stopService(i);
					if (c1.isChecked()) {
						i.putExtra("testMode", true);
						startService(i);
					}

				}
			}
		});

		int w = preference.getInt("barWidth", 30);
		widthBar = (SeekBar) findViewById(R.id.seekBar1);
		Toast.makeText(MainActivity.this, "width=" + w, Toast.LENGTH_LONG)
				.show();
		w = (int) (((w - 20) / 30f) * 100);

		widthBar.setProgress(w);
		widthBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int w = (int) (20 + 30 * (seekBar.getProgress() / 100f));
				Toast.makeText(MainActivity.this, "width=" + w,
						Toast.LENGTH_LONG).show();
				preference.edit().putInt("barWidth", w).commit();
				Intent i = new Intent(MainActivity.this, WindowService.class);
				stopService(i);
				i.putExtra("testMode", true);
				startService(i);
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		Intent i = new Intent(this, WindowService.class);
		stopService(i);
		if (c1.isChecked() || c2.isChecked()) {
			startService(i);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Intent i = new Intent(this, WindowService.class);
		i.putExtra("testMode", true);
		if (c1.isChecked() || c2.isChecked()) {
			stopService(i);
			startService(i);
		}
	}

}