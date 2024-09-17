package com.rakesh.peer_interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakesh.peer_interview.dto.LoginResponseDTO;
import com.rakesh.peer_interview.dto.RegisterUserDTO;
import com.rakesh.peer_interview.service.UserService;

@RestController
@RequestMapping("/api/v1/")
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("auth/signup")
	public ResponseEntity<LoginResponseDTO> createUser(@RequestBody RegisterUserDTO registerUserDTO) {
        return new ResponseEntity<>(this.userService.create(registerUserDTO), HttpStatus.CREATED);
    }
}
