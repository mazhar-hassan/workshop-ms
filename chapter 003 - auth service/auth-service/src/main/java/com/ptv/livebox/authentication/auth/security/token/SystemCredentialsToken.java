package com.ptv.livebox.authentication.auth.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 * 
 * @author Mazhar Hassan
 * @since Apr 15, 2019
 * @project sample-api
 */
public class SystemCredentialsToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;
	private String apiKey;
	private String apiSecret;
	private String ipAddress;

	public SystemCredentialsToken(Object principal, Object credentials, String apiKey, String apiSecret,
		String ipAddress) {
		super(principal, credentials);
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.ipAddress = ipAddress;
	}

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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
