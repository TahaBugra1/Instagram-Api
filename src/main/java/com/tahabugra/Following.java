package com.tahabugra;

import java.io.Serializable;

public class Following implements Serializable{

private Long pk;
	
	private String username;
	
	private String fullname;
	
	public Following() {
		
	}

	public Following(Long pk, String username, String fullname) {
		this.pk = pk;
		this.username = username;
		this.fullname = fullname;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}
