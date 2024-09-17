package com.rakesh.peer_interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rakesh.peer_interview.dto.LoginResponseDTO;
import com.rakesh.peer_interview.dto.RegisterUserDTO;
import com.rakesh.peer_interview.model.MyUser;
import com.rakesh.peer_interview.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public LoginResponseDTO create(RegisterUserDTO registerUserDTO) {
		MyUser user = new MyUser();
		
		System.out.println(registerUserDTO);
		user.setUsername(registerUserDTO.getUsername());
		user.setPassword(encoder.encode(registerUserDTO.getPassword()));
		user.setEnabled(registerUserDTO.getEnabled());
		
		this.repository.save(user);
		
		return new LoginResponseDTO(user.getUsername());
	}
}
