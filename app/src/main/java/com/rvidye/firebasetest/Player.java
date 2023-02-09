package com.rvidye.firebasetest;

public class Player extends User
{
	public int getVoteIndex() {
		return voteIndex;
	}

	public float getBalance() {
		return balance;
	}

	public void setVoteIndex(int voteIndex) {
		this.voteIndex = voteIndex;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	private int voteIndex; // 0 - 9
	private float balance;
	private float bet;

	public float getBet() {
		return bet;
	}

	public void setBet(float bet) {
		this.bet = bet;
	}

	public  Player()
	{
		super();
		this.balance = 0.0f;
	}

	public Player(boolean agent, String name,float balance) {
		super(false,name);
		this.balance = balance;
	}

	public void vote(int id, float value) throws Exception {
		// set voteIndex to id
		this.voteIndex = id;
		if(value > this.balance)
			throw new Exception();
		else
		{
			balance = balance - value;
			this.bet = value;
		}
		// Update vote & Balance inside database
	}
}
