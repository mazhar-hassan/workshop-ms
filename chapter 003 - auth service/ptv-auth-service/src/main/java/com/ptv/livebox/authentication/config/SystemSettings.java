package com.ptv.livebox.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemSettings {

	@Value("${p12.keystore.password}")
	private String keystorePassword;

	@Value("${p12.keystore.alias}")
	private String keystoreAlias;

	@Value("${jwt.secret}")
	private String jwtSecretKey;

	@Value("${jwt.expiration}")
	private Long jwtExpiration;

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getKeystoreAlias() {
		return keystoreAlias;
	}

	public void setKeystoreAlias(String keystoreAlias) {
		this.keystoreAlias = keystoreAlias;
	}

	public String getJwtSecretKey() {
		return jwtSecretKey;
	}

	public void setJwtSecretKey(String jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
	}

	public Long getJwtExpiration() {
		return jwtExpiration;
	}

	public void setJwtExpiration(Long jwtExpiration) {
		this.jwtExpiration = jwtExpiration;
	}

}
