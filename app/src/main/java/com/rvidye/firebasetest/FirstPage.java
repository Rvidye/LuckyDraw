package com.rvidye.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstPage extends AppCompatActivity {

	private DatabaseReference dbRef;
	private Button loginBtn;
	private EditText loginET;
	private boolean admin;
	private boolean loginPass;

	private ValueEventListener userDataListner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_page);

		loginBtn = findViewById(R.id.sfidbtn);
		loginET = findViewById(R.id.sfid);

		dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
	}

	public void onLoginClick(View v)
	{
		String userid = loginET.getText().toString();
		//admin = false;
		//loginPass = false;
		Intent adminActivity = new Intent(this,MainActivity.class);
		Intent playActivity = new Intent(this,player_first.class);

		userDataListner = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot user : snapshot.getChildren())
				{
					//Log.e("IN ON LOGIN",""+user.child("id").getValue().toString());
					if(user.child("id").getValue().toString().equals(userid))
					{
						loginPass = true;
						admin = Boolean.parseBoolean(user.child("agent").getValue().toString());
						//Log.e("IN ON LOGIN","In IF"+loginPass+admin);
						break;
					}else
					{
						loginPass = false;
						admin = false;
						//Log.e("IN ON LOGIN","in else"+loginPass+admin);
					}
				}

				if(loginPass)
				{
					if(admin)
					{
						startActivity(adminActivity);
					}
					else
					{
						playActivity.putExtra("pid",userid);
						startActivity(playActivity);
					}
				}
				else {
					Log.e("LOG IN STATUS","Fail");
					Toast.makeText(getApplicationContext(), "Log in Failed",Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		};
		dbRef.child("users").addValueEventListener(userDataListner);
	}

	@Override
	protected void onPause() {
		super.onPause();
		dbRef.child("users").removeEventListener(userDataListner);
	}
}