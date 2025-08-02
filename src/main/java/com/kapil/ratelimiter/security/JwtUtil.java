package com.kapil.ratelimiter.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Use environment variables in production
    private final long TOKEN_VALIDITY = 1000 * 60 * 60 * 10; // 10 hours
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // Generate token using a username (or userId)
    public String generateToken(String username) {
        logger.info("Generating token for user: {}", username);
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
        	    .setClaims(claims)
        	    .setSubject(subject)
        	    .setIssuedAt(new Date(System.currentTimeMillis()))
        	    .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
        	    .signWith(key)
        	    .compact();
    }

    // Extract username (subject) from token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        logger.info("Token is valid");
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Check expiration
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Extract all claims
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
