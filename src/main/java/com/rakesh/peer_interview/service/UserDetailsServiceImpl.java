package com.rakesh.peer_interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rakesh.peer_interview.model.MyUser;
import com.rakesh.peer_interview.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("[loadUserByUsername()] : Request made to database for username authentication");
		MyUser myUser = userRepository.findByUsername(username);
		if (myUser == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
		
		return User.builder()
				.username(myUser.getUsername())
				.password(myUser.getPassword())
				.authorities("ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
				.build();
	}

}
