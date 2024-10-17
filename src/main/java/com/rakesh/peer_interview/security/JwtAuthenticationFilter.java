package com.rakesh.peer_interview.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(
				HttpServletRequest request, 
				HttpServletResponse response, 
				FilterChain filterChain
			)throws ServletException, IOException {
		
		 String jwtToken = extractTokenFromRequest(request);
		 
		 try {
			 if(jwtToken != null) {
				 String username = this.jwtService.getUsernameFromToken(jwtToken);
				 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				 
				 if(username != null && authentication == null) {
					 UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = createAuthToken(request, userDetails);
					 Boolean isTokenValid = jwtService.isTokenValid(jwtToken, userDetails);
					 if(isTokenValid)SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				 } 
			 }
		 }catch(ExpiredJwtException ex) {
			 logger.error("JWT token has expired: {}", ex.getMessage());
			 response.setContentType("application/json");
			 response.setStatus(HttpStatus.UNAUTHORIZED.value());
             response.getWriter().write("token expired");
             return;
		 }
		 filterChain.doFilter(request, response);
	}
	
	private String extractTokenFromRequest(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) return null;
		return authorizationHeader.substring(7);
	}
	
	
	private UsernamePasswordAuthenticationToken createAuthToken(HttpServletRequest request, UserDetails userDetails) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authenticationToken;
	}
	
}