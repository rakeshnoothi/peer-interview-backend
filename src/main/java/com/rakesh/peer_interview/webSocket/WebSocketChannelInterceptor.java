package com.rakesh.peer_interview.webSocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.rakesh.peer_interview.security.JwtService;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		System.out.println("Websocket channel interceptor ran");
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
			String token = authorizationHeader.substring(7);
			
			String username = jwtService.getUsernameFromToken(token);
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			accessor.setUser(usernamePasswordAuthenticationToken);
		}

		return message;
	}
}
