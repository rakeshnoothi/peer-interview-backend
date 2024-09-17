package com.rakesh.peer_interview.dto;

public class RegisterUserDTO {
	private String username;
	private String password;
	private Boolean enabled;
	
	public RegisterUserDTO() {}

	public RegisterUserDTO(String username, String password, Boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
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
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "RegisterUserDTO [username=" + username + ", password=" + password+ ", enabled=" + enabled + "]";
	}

	
	
	
}
