package com.ptv.livebox.authentication.auth;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 * 
 * @author Mazhar Hassan
 * @since Apr 15, 2019
 * @project sample-api
 */
public class UserInfo {

	private Long userId;
	private String username;
	private String fullName;
	private String usersecret;
	private String ipAddress;

	public UserInfo() {

	}

	public UserInfo(Long userId, String fullName, String usersecret, String ipAddress) {
		super();
		this.ipAddress = ipAddress;
		this.userId = userId;
		this.fullName = fullName;
		this.usersecret = usersecret;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsersecret() {
		return usersecret;
	}

	public void setUsersecret(String usersecret) {
		this.usersecret = usersecret;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
