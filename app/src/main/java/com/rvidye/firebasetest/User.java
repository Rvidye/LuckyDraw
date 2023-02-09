package com.rvidye.firebasetest;

import java.util.UUID;

public class User {

	private String id;
	private boolean agent;
	private String name;

	public User(boolean agent, String name) {
		this.id = new String(UID.UID(4));
		this.agent = agent;
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}

	public User()
	{
	}

	public boolean isAgent() {
		return agent;
	}

	public String getName()
	{
		return name;
	}
}
