package com.rakesh.peer_interview.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.expiresMs}")
	private Long expiersMs;
	
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	
	public Long getExpiresInMs() {
		return this.expiersMs;
	}
	
	public String getUsernameFromToken(String token) {
		Claims claims = getAllClaimsFromToken(token);
		System.out.println("All claims recevied");
		return claims.getSubject();
	}
	
	private Date getExpirationDateFromToken(String token) {
		Claims claims = getAllClaimsFromToken(token);
		return claims.getExpiration();
	}
	
	private boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
	}
	
	// get all claims from the token.
	private Claims getAllClaimsFromToken(String token) {
			return Jwts.parser()
					.verifyWith(getSignatureKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
	}
	
	// Build JWT token with claims
	 public String buildJwtToken(String username) {
		 Long expirationTime = System.currentTimeMillis() + expiersMs;
		 return Jwts.builder()
				    .issuer("me")
				    .subject(username)
				    .issuedAt(new Date())
				    .expiration(new Date(expirationTime))
				    .signWith(getSignatureKey())
				    .compact();
	 }
	 
	 // Generate a key for signature
	 private SecretKey getSignatureKey() {
		 return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
	 }
	 
	 // validate the token
	 public boolean isTokenValid(String token, UserDetails userDetails) {
		 String username = getUsernameFromToken(token);
		 return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	 }
}