package com.rvidye.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class player_first extends AppCompatActivity {

	private String pid;
	private String sid;

	private Button sstart;
	private EditText sesid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_first);

		Bundle extras = getIntent().getExtras();
		if(extras == null)
			Log.e("Error","extras null");
		else {
			Log.d("pid", extras.getString("pid"));
			pid = extras.getString("pid");
		}
		sesid = findViewById(R.id.sfid);
		sstart = findViewById(R.id.sfidbtn);
	}

	public void onSessionClick(View v)
	{
		sid = sesid.getText().toString();
		if(sid.equals("") || pid.equals(""))
			Toast.makeText(this,"Invalid Player Id or Session ID",Toast.LENGTH_LONG).show();
		else
		{
			Intent player = new Intent(this,PlayerActivity.class);
			player.putExtra("sid",sid);
			player.putExtra("pid",pid);
			startActivity(player);
		}
	}

}