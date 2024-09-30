package com.rakesh.peer_interview.security.dto;

import java.util.Date;

public class LoginResponseDTO {
	private String token;
	private String tokenType;
	private Long expiresMs;
	private Date issuedAt;
	
	public LoginResponseDTO() {}

	public LoginResponseDTO(String token, Long expiresMs, String tokenType, Date issuedAt) {
		this.token = token;
		this.expiresMs = expiresMs;
		this.tokenType = tokenType;
		this.issuedAt = issuedAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpiresMs() {
		return expiresMs;
	}

	public void setExpiresMs(Long expiresMs) {
		this.expiresMs = expiresMs;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	public Date getIssuedAt() {
		return this.issuedAt;
	}
	
	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}

	@Override
	public String toString() {
		return "LoginResponseDTO [token=" + token + ", tokenType=" + tokenType + ", expiresMs=" + expiresMs
				+ ", issuedAt=" + issuedAt + "]";
	}

}
