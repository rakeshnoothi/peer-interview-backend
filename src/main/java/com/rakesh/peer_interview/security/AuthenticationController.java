package com.rakesh.peer_interview.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakesh.peer_interview.model.User;
import com.rakesh.peer_interview.security.dto.LoginRequestDTO;
import com.rakesh.peer_interview.security.dto.LoginResponseDTO;
import com.rakesh.peer_interview.security.dto.RegisterUserRequestDTO;
import com.rakesh.peer_interview.security.dto.RegisterUserResponseDTO;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/signup")
	public ResponseEntity<RegisterUserResponseDTO> createUser(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
		return new ResponseEntity<RegisterUserResponseDTO>(this.userService.create(registerUserRequestDTO), HttpStatus.CREATED);
    }
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
		User user = authenticate(loginRequestDTO.getUsername(),loginRequestDTO.getPassword());
		
		String jwtToken = jwtService.buildJwtToken(user.getUsername());
		
		LoginResponseDTO loginResponse = new LoginResponseDTO();
		loginResponse.setToken(jwtToken);
		loginResponse.setTokenType("Bearer");
		loginResponse.setExpiresMs(jwtService.getExpiresInMs());
		loginResponse.setIssuedAt(new Date());
		
		return new ResponseEntity<LoginResponseDTO>(loginResponse, HttpStatus.OK);
	}
	
	private User authenticate (String username, String password) {
		// This is an authentication object
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		try {
			// authenticates the user.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return (User) authentication.getPrincipal();
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException("Invalid username or password!!");
		}
	}
}
