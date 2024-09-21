package com.rakesh.peer_interview.security.dto;

public class LoginResponseDTO {
	private String username;
	private String token;
	private Long expiresIn;
	
	public LoginResponseDTO() {}

	public LoginResponseDTO(String username, String token, Long expiresIn) {
		this.username = username;
		this.token = token;
		this.expiresIn = expiresIn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return "LoginResponseDTO [username=" + username + ", token=" + token + ", expiresIn=" + expiresIn + "]";
	}

}
