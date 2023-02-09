package com.rvidye.firebasetest;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Agent extends User
{
	private FirebaseDatabase db;
	private DatabaseReference dbRef;
	private Session game;

	public void AddPlayerToSession(String name, float balance)
	{
		game.AddPlayer(new Player(false,name,balance));
	}

	public void CreateSession(int milliseconds)
	{
		// Create Local Session object
		game = new Session(milliseconds);
		// Update Session details in db
		dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
		dbRef.child("session").child(game.getId()).child("id").setValue(game.getId());
		dbRef.child("session").child(game.getId()).child("time").setValue(milliseconds);
		dbRef.child("session").child(game.getId()).child("winner").setValue("");
		dbRef.child("session").child(game.getId()).child("isStart").setValue(false);
	}

	public Session getGame() {
		return game;
	}
}
