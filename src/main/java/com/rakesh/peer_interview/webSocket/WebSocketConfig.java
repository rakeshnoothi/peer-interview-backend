package com.rakesh.peer_interview.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.rakesh.peer_interview.security.JwtService;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Value("${allowedOrigin}")
	private String allowedOrigin;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// hit this end-point for web sockets handshake.
		registry.addEndpoint("/api/v1/ws")
				.setAllowedOrigins("*");
		
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes("/app"); 
		config.enableSimpleBroker("/topic", "/queue"); 
		config.setUserDestinationPrefix("/user");
	}
	
	@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
        	@Override
        	public Message<?> preSend(Message<?> message, MessageChannel channel) {
        		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
        			System.out.println("Websocket channel interceptor ran");
        			String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
        			
        				if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        					String token = authorizationHeader.substring(7);
                			
                			String username = jwtService.getUsernameFromToken(token);
                			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                			
                			boolean isTokenValid = jwtService.isTokenValid(token, userDetails);
                			System.out.println("Is token valid: " + isTokenValid);
                			
                			if(isTokenValid) {
                				System.out.println("The Provided token is valid");
                				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    			accessor.setUser(usernamePasswordAuthenticationToken);
                			}
        				}
        		}

        		return message;
        	}
        });
    }
}
