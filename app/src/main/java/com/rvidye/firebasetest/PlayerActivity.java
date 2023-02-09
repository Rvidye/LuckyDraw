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

	Button b0,b1,b2,b3,b4,b5,b6,b7,b8,b9,votebtn;
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
	Intent gamei ;

	private ValueEventListener userValeListner;
	private ValueEventListener sessionValeListner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		gamei = new Intent(this,GameInProgress.class);
		b0 = findViewById(R.id.b0);
		b1 = findViewById(R.id.b1);
		b2 = findViewById(R.id.b2);
		b3 = findViewById(R.id.b3);
		b4 = findViewById(R.id.b4);
		b5 = findViewById(R.id.b5);
		b6 = findViewById(R.id.b6);
		b7 = findViewById(R.id.b7);
		b8 = findViewById(R.id.b8);
		b9 = findViewById(R.id.b9);
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
				if(canVote)
				{
					gamei.putExtra("sid",psession.getId());
					gamei.putExtra("time",""+timeinml);
					startActivity(gamei);
				}
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
		Button b = (Button) v;
		switch (b.getText().toString())
		{
			case "0":
				vote = 0;
			break;
			case "1":
				vote = 1;
			break;
			case "2":
				vote = 2;
			break;
			case "3":
				vote = 3;
			break;
			case "4":
				vote = 4;
			break;
			case "5":
				vote = 5;
			break;
			case "6":
				vote = 6;
			break;
			case "7":
				vote = 7;
			break;
			case "8":
				vote = 8;
			break;
			case "9":
				vote = 9;
			break;
			default:
				vote = -1;
			break;
		}
		votetv.setText("Vote :: "+vote);
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
				Log.e("Error","Value Invalid");

			if(player.getBalance() <= 0)
				Log.e("Error","Not enough balance");

			if(vote == -1)
				Log.e("Error","Vote invalid");

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
		if(canVote)
			checkWinner();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbRef.removeEventListener(userValeListner);
		dbRef.removeEventListener(sessionValeListner);
	}
}