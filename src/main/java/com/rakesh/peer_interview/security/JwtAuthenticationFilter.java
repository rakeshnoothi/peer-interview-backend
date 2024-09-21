package com.rakesh.peer_interview.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

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
		
		 
		 String requestHeader = request.getHeader("Authorization");
		 
		 // check if the header contains an JWT token. 
		 if(requestHeader != null && requestHeader.startsWith("Bearer ")) {
			 String jwtToken = requestHeader.substring(7);
			 String username = this.jwtService.getUsernameFromToken(jwtToken);
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			 
			 if(username != null && authentication == null) {
				 UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				 Boolean isTokenValid = jwtService.isTokenValid(jwtToken, userDetails);
				 
				 if(isTokenValid) {
					 	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						System.out.println("security context before: " + SecurityContextHolder.getContext().getAuthentication());
			            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			            System.out.println("security context after: " + SecurityContextHolder.getContext().getAuthentication());
				 }
			 }
		 }
		 /*  checking the security context for authentication object because security context clears
		    for every request 
		 */
		 filterChain.doFilter(request, response);
	}

}
