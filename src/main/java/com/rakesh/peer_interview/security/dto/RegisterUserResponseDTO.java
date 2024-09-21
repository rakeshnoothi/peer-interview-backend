package com.rakesh.peer_interview.security.dto;

public class RegisterUserResponseDTO {
	private String username;
	
	public RegisterUserResponseDTO() {}
	
	public RegisterUserResponseDTO(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
