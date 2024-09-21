package com.rakesh.peer_interview.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {
	
	@GetMapping("/home")
	public String test() {
		System.out.println("[GET]: Request made to /home URL");
		return "Hello";
	}
}
