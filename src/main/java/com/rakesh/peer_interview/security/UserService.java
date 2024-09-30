package com.rakesh.peer_interview.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rakesh.peer_interview.model.User;
import com.rakesh.peer_interview.repository.UserRepository;
import com.rakesh.peer_interview.security.dto.RegisterUserRequestDTO;
import com.rakesh.peer_interview.security.dto.RegisterUserResponseDTO;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public RegisterUserResponseDTO create(RegisterUserRequestDTO registerUserRequestDTO) {
		User user = new User();
		
		System.out.println(registerUserRequestDTO);
		user.setUsername(registerUserRequestDTO.getUsername());
		user.setPassword(encoder.encode(registerUserRequestDTO.getPassword()));
		user.setEnabled(registerUserRequestDTO.getEnabled());
		System.out.println("NEW USER: " + user);
		
		this.repository.save(user);
		
		return new RegisterUserResponseDTO(user.getUsername());
	}
}
