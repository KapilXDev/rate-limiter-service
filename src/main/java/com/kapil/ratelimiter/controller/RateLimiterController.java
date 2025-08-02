package com.kapil.ratelimiter.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kapil.ratelimiter.security.JwtUtil;
import com.kapil.ratelimiter.service.RateLimiterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Rate Limiter APIs", description = "APIs to authenticate and access rate-limited endpoints")
public class RateLimiterController {
	
	private static final Logger logger = LoggerFactory.getLogger(RateLimiterController.class);
	
	@Autowired
	private RateLimiterService rateLimiter;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Operation(summary = "Get rate-limited data", description = "Returns data if user hasn't exceeded their rate limit")
	@GetMapping("/api/data")
	public ResponseEntity<?> getData(@Parameter(description = "JWT token in the format: Bearer <token>")
			@RequestHeader("Authorization") String token) {
		String userId = jwtUtil.extractUsername(token.replace("Bearer ", "").trim());
	    logger.info("Incoming request from user: {}", userId);
	    if (!rateLimiter.isAllowed(userId)) {
	        logger.warn("User {} exceeded rate limit", userId);
	        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
	    }

	    return ResponseEntity.ok("Data fetched successfully");
	}
	
	@Operation(summary = "Authenticate user and return JWT token")
	@PostMapping("/auth")
	public ResponseEntity<Map<String, String>> authenticate(@Parameter(description = "Unique username for token generation")
				@RequestParam String username) {
	    logger.info("Authenticating user: {}", username);
	    String token = jwtUtil.generateToken(username);
	    Map<String, String> response = new HashMap<>();
	    response.put("token", token);
	    return ResponseEntity.ok(response);
	}
    
	@Operation(summary = "Welcome Page")
    @GetMapping("/")
    public String home() {
        return "Welcome to the Rate Limiter API. Use /api/resource?userId=YOUR_ID";
    }
}