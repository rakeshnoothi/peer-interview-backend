package com.rakesh.peer_interview.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterUserRequestDTO {
	private String username;
	private String password;
	private Boolean enabled;
	private String firstName;
	private String lastName;	
}
