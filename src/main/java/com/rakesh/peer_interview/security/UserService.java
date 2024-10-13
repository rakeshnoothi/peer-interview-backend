package com.rakesh.peer_interview.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rakesh.peer_interview.entity.User;
import com.rakesh.peer_interview.httpResponseUtil.CustomResponse;
import com.rakesh.peer_interview.repository.UserRepository;
import com.rakesh.peer_interview.security.dto.LoginRequestDTO;
import com.rakesh.peer_interview.security.dto.LoginResponseDTO;
import com.rakesh.peer_interview.security.dto.RegisterUserRequestDTO;
import com.rakesh.peer_interview.security.dto.RegisterUserResponseDTO;
import com.rakesh.peer_interview.security.dto.UserDTO;

@Service
public class UserService {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public CustomResponse create(RegisterUserRequestDTO registerUserRequestDTO) {
		
		User user = new User();
		
		System.out.println(registerUserRequestDTO);
		
		user.setUsername(registerUserRequestDTO.getUsername());
		user.setPassword(encoder.encode(registerUserRequestDTO.getPassword()));
		user.setEnabled(registerUserRequestDTO.getEnabled());
		user.setFirstName(registerUserRequestDTO.getFirstName());
		user.setLastName(registerUserRequestDTO.getLastName());
		
		System.out.println("NEW USER: " + user);
		
		this.repository.save(user);
		
		return CustomResponse.getResponse(HttpStatus.CREATED.value(), "new user registered", new RegisterUserResponseDTO(user.getUsername()));
	}
	
	public CustomResponse login(LoginRequestDTO loginRequestDTO) {
		User user = authenticate(loginRequestDTO.getUsername(),loginRequestDTO.getPassword());
		
		String jwtToken = jwtService.buildJwtToken(user.getUsername());
		
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setUsername(user.getUsername());
		
		LoginResponseDTO loginResponse = LoginResponseDTO
											.builder()
											.token(jwtToken)
											.tokenType("Bearer")
											.expiresMs(jwtService.getExpiresInMs())
											.issuedAt(new Date())
											.userDTO(userDTO)
											.build();
		
		return CustomResponse.getResponse(HttpStatus.OK.value(), "user login successful", loginResponse);
		
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