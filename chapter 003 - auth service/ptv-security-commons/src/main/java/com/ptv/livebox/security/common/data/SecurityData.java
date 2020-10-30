package com.ptv.livebox.security.common.data;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 * 
 * @author Mazhar Hassan
 * @since Apr 15, 2019
 * @project sample-api
 */
public class SecurityData {
	private String type;
	private String apiKey;
	private String apiSecret;
	private String token;
	private String username;
	private String password;
	private String xcode;
	private String ipAddress;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCredential() {
		return "credentials".equals(type);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getXcode() {
		return xcode;
	}

	public void setXcode(String xcode) {
		this.xcode = xcode;
	}
}
