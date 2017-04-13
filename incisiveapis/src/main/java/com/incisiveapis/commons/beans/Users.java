package com.incisiveapis.commons.beans;

import java.util.List;

public class Users {
	private List<User> users;
	private MessageElements message;
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public MessageElements getMessage() {
		return message;
	}
	public void setMessage(MessageElements message) {
		this.message = message;
	}

}
