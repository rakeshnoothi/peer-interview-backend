package com.rakesh.peer_interview.dto;

public class LoginResponseDTO {
	private String username;
	

	public LoginResponseDTO() {}

	public LoginResponseDTO(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "LoginResponseDTO [username=" + username + "]";
	}
	
	
}
