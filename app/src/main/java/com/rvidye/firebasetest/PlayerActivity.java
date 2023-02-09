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

public class PlayerActivity extends AppCompatActivity{

	Button votebtn;
	TextView sid,pid,pname,votetv,balance;
	EditText betet;
	private DatabaseReference dbRef;
	int vote = -1;
	float value = 0.0f;
	Player player;
	String spid = "1";
	String sessid = "0";
	Session psession;
	long timeinml;
	boolean canVote = true;
	String winnerString = "0";
	Intent gamei;

	private ValueEventListener userValeListner;
	private ValueEventListener sessionValeListner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		gamei = new Intent(this,GameInProgress.class);
		votebtn = findViewById(R.id.votebtn);
		sid = findViewById(R.id.sesid);
		pid = findViewById(R.id.pid);
		pname = findViewById(R.id.pname);
		votetv = findViewById(R.id.votetv);
		betet = findViewById(R.id.bettv);
		balance = findViewById(R.id.balanceid);

		Bundle extras = getIntent().getExtras();
		if(extras == null)
			Log.e("Error","extras null");
		else {
			Log.d("sid", extras.getString("sid"));
			sessid = extras.getString("sid");
			sid.setText("Session ID : "+sessid);
			Log.d("pid", extras.getString("pid"));
			spid = extras.getString("pid");
			pid.setText("Player ID : "+spid);
		}
		player = new Player();
		psession = new Session();

		userValeListner = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for(DataSnapshot child : snapshot.getChildren()) {
					Log.d("IN DB CHANGE",child.getValue().toString());
					if(child.child("id").exists() && child.child("name").exists() && child.child("balance").exists() && child.child("vote").exists() && child.child("agent").exists())
					{
						Log.d("IN IF",child.child("id").toString());
						if(child.child("id").getValue().toString().equals(spid))
						{
							Log.d("IN IF","2");
							player.setId(spid);
							player.setName(child.child("name").getValue().toString());
							player.setBalance(Float.parseFloat(child.child("balance").getValue().toString()));
							player.setBet(Float.parseFloat(child.child("bet").getValue().toString()));
							player.setVoteIndex(Integer.parseInt(child.child("vote").getValue().toString()));
						}
					}
				}
				pname.setText("Name : "+player.getName());
				balance.setText("Balance : "+player.getBalance());
			}
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		};

		sessionValeListner = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for(DataSnapshot child : snapshot.getChildren()) {
					//Log.d("IN DB CHANGE SESSION",child.getValue().toString());
					if(child.child("id").exists())
					{
						//Log.d("IN IF",child.child("id").toString());
						if(child.child("id").getValue().toString().equals(sessid))
						{
							//Log.d("IN IF","2");
							psession.setId(child.child("id").getValue().toString());
							timeinml = Long.parseLong(child.child("time").getValue().toString());
							canVote = Boolean.parseBoolean(child.child("isStart").getValue().toString());
							winnerString = child.child("winner").getValue().toString();
						}
					}
				}
				//Log.d("Session Player :",""+psession.getId()+" "+timeinml+" "+canVote+" "+winnerString);
			}
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		};

		dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
		dbRef.child("users").addValueEventListener(userValeListner);
		dbRef.child("session").addValueEventListener(sessionValeListner);
	}

	public void setVote(View v)
	{
		//Button b = (Button) v;
		switch (v.getId())
		{
			case R.id.grah:
				vote = 0;
				votetv.setText("Vote :: Grah");
			break;
			case R.id.vastu:
				vote = 1;
				votetv.setText("Vote :: Vastu");
			break;
			case R.id.vashikaran:
				vote = 2;
				votetv.setText("Vote :: Vashikaran");
			break;
			case R.id.tara:
				vote = 3;
				votetv.setText("Vote :: Tara");
			break;
			case R.id.sudarshan:
				vote = 4;
				votetv.setText("Vote :: Sudarshan");
			break;
			case R.id.shree:
				vote = 5;
				votetv.setText("Vote :: Shree");
			break;
			case R.id.planet:
				vote = 6;
				votetv.setText("Vote :: Planet");
			break;
			case R.id.meditation:
				vote = 7;
				votetv.setText("Vote :: Meditation");
			break;
			case R.id.love:
				vote = 8;
				votetv.setText("Vote :: Love");
			break;
			case R.id.matsya:
				vote = 9;
				votetv.setText("Vote :: Matsya");
			break;
			default:
				vote = -1;
				votetv.setText("Vote :: Invalid");
			break;
		}
	}

	public void RegisterVote(View v)
	{
		//agent.AddPlayerToSession(name.getText().toString(),Float.parseFloat(balance.getText().toString()));
		dbRef.removeEventListener(userValeListner);
		dbRef.removeEventListener(sessionValeListner);
		if(!canVote)
		{
			// create new timer
			value = Float.parseFloat(betet.getText().toString());
			if(value < 100 || value > player.getBalance())
			{
				Toast.makeText(this,"Value Invalid",Toast.LENGTH_SHORT).show();
				Log.e("Error","Value Invalid");
				return;
			}

			if(player.getBalance() <= 0)
			{
				Toast.makeText(this,"Not enough balance",Toast.LENGTH_SHORT).show();
				Log.e("Error","Not enough balance");
				return;
			}

			if(vote == -1)
			{
				Toast.makeText(this,"Vote invalid",Toast.LENGTH_SHORT).show();
				Log.e("Error","Vote invalid");
				return;
			}

			player.setBalance(player.getBalance() - value);
			dbRef.child("users").child(player.getId()).child("balance").setValue(player.getBalance());
			dbRef.child("users").child(player.getId()).child("bet").setValue(value);
			dbRef.child("users").child(player.getId()).child("vote").setValue(vote);
			canVote = true;
			votebtn.setEnabled(false);
			betet.setEnabled(false);
			gamei.putExtra("sid",psession.getId());
			gamei.putExtra("time",""+timeinml);
			startActivity(gamei);
			//checkWinner();
		}
		else
		{
			Toast.makeText(this,"Cannot Vote Now : ",Toast.LENGTH_LONG).show();
		}
	}

	public void checkWinner()
	{
		dbRef.child("session").addValueEventListener(sessionValeListner);
		if(winnerString.contains(spid))
		{
			Toast.makeText(this,"You Won",Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this,"You Lost",Toast.LENGTH_LONG).show();
		}
		finish();
		dbRef.removeEventListener(sessionValeListner);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//if(canVote)
			//checkWinner();
	}

	@Override
	public void onBackPressed() {
		//
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbRef.removeEventListener(userValeListner);
		dbRef.removeEventListener(sessionValeListner);
	}
}