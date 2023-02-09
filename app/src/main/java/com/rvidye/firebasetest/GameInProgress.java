package com.rvidye.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;

public class GameInProgress extends AppCompatActivity {

	private String sid;
	private String time;
	private String agent;
	private TextView gsid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_in_progress);

		Bundle extras = getIntent().getExtras();
		if(extras == null)
			Log.e("Error","extras null");
		else
		{
			Log.d("sid", extras.getString("sid"));
			sid = extras.getString("sid");
			Log.d("Time In Mins", extras.getString("time"));
			time = extras.getString("time");
			agent = extras.getString("agent");
		}
		gsid = findViewById(R.id.gsid);

		gsid.setText("Session ID : "+sid);

		long t = Long.parseLong(time);

		CountDownTimer timer = new CountDownTimer(t*60000,1000) {
			@Override
			public void onTick(long l) {
				//Log.e("Timer",""+l/1000);
			}

			@Override
			public void onFinish() {
				//Log.e("Timer","Done");
				finish();
			}
		};
		timer.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}