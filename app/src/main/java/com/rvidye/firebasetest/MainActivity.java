package com.rvidye.firebasetest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
	Agent agent;
	Button sgbtn,csbtn,apbtn;
	TextView timer,name,balance,sessid,playerid;
	private DatabaseReference dbRef;
	Session game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sgbtn = findViewById(R.id.gamestartbtn);
		csbtn = findViewById(R.id.cs);
		apbtn = findViewById(R.id.button2);
		timer = findViewById(R.id.timertv);
		name = findViewById(R.id.nametv);
		balance = findViewById(R.id.balancetv);
		sessid = findViewById(R.id.sid);
		playerid = findViewById(R.id.pidtv);
		//agent = new Agent();
	}

	public void onCreateSession(View v)
	{
		int ml = Integer.parseInt(timer.getText().toString());
		//agent.CreateSession(ml);
		game = new Session(ml);

		dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
		dbRef.child("session").child(game.getId()).child("id").setValue(game.getId());
		dbRef.child("session").child(game.getId()).child("time").setValue(game.getTime());
		dbRef.child("session").child(game.getId()).child("winner").setValue("");
		dbRef.child("session").child(game.getId()).child("isStart").setValue(false);

		Toast.makeText(this,"Session ID : "+game.getId(),Toast.LENGTH_LONG).show();
		Log.d("Session ID",game.getId());
		sessid.setText("Session ID : "+game.getId());
	}

	public void onAddPlayer(View v)
	{
		//agent.AddPlayerToSession(name.getText().toString(),Float.parseFloat(balance.getText().toString()));
		String n = name.getText().toString();
		String t = balance.getText().toString();
		Player p = new Player(false,n,Float.parseFloat(t));
		playerid.setText(p.getId());
		game.AddPlayer(p);
		name.setText("");
		balance.setText("");
	}

	public void onStartGame(View v)
	{
		game.StartGame();
		Intent gamei = new Intent(this,GameInProgress.class);
		gamei.putExtra("sid",game.getId());
		gamei.putExtra("time",""+game.getTime());
		gamei.putExtra("agent","yes");
		startActivity(gamei);
	}

	@Override
	protected void onResume() {
		super.onResume();
		timer.setText("");
		name.setText("");
		balance.setText("");
		sessid.setText("");
	}

	@Override
	public void onBackPressed() {
		//
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}