package com.rakesh.peer_interview.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rakesh.peer_interview.model.User;
import com.rakesh.peer_interview.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("[loadUserByUsername()] : Request made to database for username authentication");
		User user = userRepository.findByUsername(username);
		if (user == null) throw new UsernameNotFoundException("Could not find user");
		return user;
	}

}
