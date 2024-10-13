package com.rakesh.peer_interview.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakesh.peer_interview.httpResponseUtil.CustomResponse;
import com.rakesh.peer_interview.security.dto.LoginRequestDTO;
import com.rakesh.peer_interview.security.dto.RegisterUserRequestDTO;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private UserService userService;

	
	@PostMapping("/signup")
	public ResponseEntity<CustomResponse> createUser(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
		CustomResponse customResponse = this.userService.create(registerUserRequestDTO);
		return new ResponseEntity<CustomResponse>(customResponse, HttpStatusCode.valueOf(customResponse.getStatus()));
    }
	
	@PostMapping("/login")
	public ResponseEntity<CustomResponse> login(@RequestBody LoginRequestDTO loginRequestDTO){
		CustomResponse customResponse = this.userService.login(loginRequestDTO);
		return new ResponseEntity<CustomResponse>(customResponse, HttpStatusCode.valueOf(customResponse.getStatus()));
	}
}