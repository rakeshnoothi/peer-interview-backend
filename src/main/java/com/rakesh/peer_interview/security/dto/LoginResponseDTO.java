package com.rakesh.peer_interview.security.dto;

public class LoginResponseDTO {
	private String token;
	private Long expiresIn;
	
	public LoginResponseDTO() {}

	public LoginResponseDTO(String token, Long expiresIn) {
		this.token = token;
		this.expiresIn = expiresIn;
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
		return "LoginResponseDTO [token=" + token + ", expiresIn=" + expiresIn + "]";
	}

}
