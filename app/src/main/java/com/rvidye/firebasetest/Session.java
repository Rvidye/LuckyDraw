package com.rvidye.firebasetest;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Session implements ValueEventListener{

	private String id;
	private boolean done;
	static ArrayList<Player> players;
	private float[] voteValue;
	private DatabaseReference dbRef;
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Session() {
		id = "0000";
		done = false;
		voteValue = new float[10];
		for(int i = 0; i  < voteValue.length; i++)
			voteValue[i] = 9999;
		time = 0;
		players = new ArrayList<Player>();
		//dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
	}

	public Session(int milliseccond ) {
		this.id = new String(UID.UID(4));
		done =  false;
		time = milliseccond;
		voteValue = new float[10];
		for(int i = 0; i  < voteValue.length; i++)
			voteValue[i] = 9999;
		players = new ArrayList<Player>();
		//dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void StartGame()
	{

		ArrayList<String> winner = new ArrayList<String>();
		dbRef.child("session").child(this.getId()).child("isStart").setValue(true);
		if(players.isEmpty())
			Log.e("Error","No player in session !!!");
		else
		{
			for(Player p : players)
			{
				Log.d("In Start game : ",p.getId()+" "+p.getName() +" "+p.getBalance()+" "+p.getVoteIndex());
				if(voteValue[p.getVoteIndex()] == 9999)
					voteValue[p.getVoteIndex()] = 0;
				voteValue[p.getVoteIndex()] += p.getBet();
			}
		}

		// Find Card With Least Amount of Bet And Select It
		int idx = 0;
		float min = voteValue[idx];
		Log.d("Vote",""+voteValue[idx]);
		for(int i = 1; i < voteValue.length;i++)
		{
			Log.d("Vote",""+voteValue[i]);
			if(voteValue[i] <= min )
			{
				min = voteValue[i];
				idx = i;
			}
		}

		Log.d("Winner Card",idx+"");
		// Check which player bet on idx
		/*
		*	for player in players
		* 		if(player.vote = idx)
		* 			winner = player.id;
		*/
		for(Player p : players){
			if(p.getVoteIndex() == idx){
				Log.d("Winners",""+p.getId());
				winner.add(p.getId());
				//push to db
			}
		}

		dbRef.child("session").child(this.getId()).child("winner").setValue(winner);
		/*
		// remove all players from db and list
		for(Player p : players)
		{
			dbRef.child("users").child(p.getId()).removeValue();
			//players.remove(p);
		}
		*/
		players.clear();
		dbRef.child("users").removeEventListener(this);
		//return  winner;
	}

	public void AddPlayer(Player p)
	{
		// Create new player object

		// Insert player into realtime database
		dbRef = FirebaseDatabase.getInstance("https://databasetest-108f5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
		dbRef.child("users").addValueEventListener(this);
		dbRef.child("users").child(p.getId()).child("id").setValue(p.getId());
		dbRef.child("users").child(p.getId()).child("name").setValue(p.getName());
		dbRef.child("users").child(p.getId()).child("balance").setValue(p.getBalance());
		dbRef.child("users").child(p.getId()).child("bet").setValue(p.getBet());
		dbRef.child("users").child(p.getId()).child("vote").setValue(p.getVoteIndex());
		dbRef.child("users").child(p.getId()).child("agent").setValue(p.isAgent());
		// Add player to static session
		players.add(p);
	}

	@Override
	public void onDataChange(@NonNull DataSnapshot snapshot) {
		//Log.d("On Data Changed:", );
		for(DataSnapshot child : snapshot.getChildren()) {

			if(child.child("id").exists() && child.child("name").exists() && child.child("balance").exists() && child.child("vote").exists() && child.child("agent").exists())
			{
				Log.d("Data Changed",child.getValue().toString());
				for(Player p : players)
				{
					if(child.child("id").getValue().toString().equals(p.getId()))
					{
						p.setBalance(Float.parseFloat(child.child("balance").getValue().toString()));
						p.setVoteIndex(Integer.parseInt(child.child("vote").getValue().toString()));
						p.setBet(Float.parseFloat(child.child("bet").getValue().toString()));
					}
				}
			}
		}
	}

	@Override
	public void onCancelled(@NonNull DatabaseError error) {

	}
}
