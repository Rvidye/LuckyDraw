package com.rvidye.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameFinished extends AppCompatActivity {

	TextView winnerTV,sessionidTV;
	DatabaseReference dbRef;
	ValueEventListener winnerListner;

	private String sid;
	private String winners;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_finished);

		sessionidTV = findViewById(R.id.gosesid);
		winnerTV = findViewById(R.id.winner);

		Bundle extras = getIntent().getExtras();
		if(extras == null)
			Log.e("Error","extras null");
		else
		{
			Log.d("sid", extras.getString("sid"));
			sid = extras.getString("sid");
		}

		winnerListner = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot child : snapshot.getChildren()) {
					//Log.d("IN DB CHANGE SESSION",child.getValue().toString());
					if (child.child("id").exists()) {
						//Log.d("IN IF",child.child("id").toString());
						if (child.child("id").getValue().toString().equals(sid)) {
							//Log.d("IN IF","2");
							winners = child.child("winner").getValue().toString();
						}
					}
				}

				sessionidTV.setText("Session ID : "+sid+" Finished");
				winnerTV.setText("Winners :: "+winners);
			}
			@Override
			public void onCancelled(@NonNull DatabaseError error) {}
		};
		dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
		dbRef.child("session").addValueEventListener(winnerListner);
	}

	@Override
	protected void onPause() {
		super.onPause();
		dbRef.child("session").removeEventListener(winnerListner);
	}

	@Override
	public void onBackPressed() {
		//
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbRef.child("session").removeEventListener(winnerListner);
	}
}