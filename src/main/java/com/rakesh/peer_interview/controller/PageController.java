package com.rakesh.peer_interview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PageController {
	
	@GetMapping("/home")
	public ResponseEntity<String> home(){
		return new ResponseEntity<>("hello", HttpStatus.OK);
	} 
}
