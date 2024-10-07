package com.rakesh.peer_interview.security.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LoginResponseDTO {
	
	private String token;
	private String tokenType;
	private Long expiresMs;
	private Date issuedAt;
	private UserDTO userDTO;

}
