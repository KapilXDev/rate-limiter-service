package com.kapil.ratelimiter.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

	private final RedisTemplate<String, String> redisTemplate;
    private final long LIMIT = 5; // max 5 requests
    private final long TIME_WINDOW = 60; // seconds
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterService.class);

    public RateLimiterService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {
        String key = "rate_limit:" + userId;
        Long count = redisTemplate.opsForValue().increment(key);
        logger.info("User {} made a request. Count = {}", userId, count);
        
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(TIME_WINDOW));
            logger.debug("Rate limit window started for user: {}", userId);
        }
        
        if (count > LIMIT) {
            logger.warn("Rate limit exceeded for user: {}", userId);
            return false;
        }

        return true;
    }
    
/*  in-memmory  
 	private static final int REQUEST_LIMIT = 5;
    private static final long TIME_WINDOW_MILLIS = 60 * 1000; // 1 minute

    private final Map<String, LinkedList<Long>> userRequestLog = new ConcurrentHashMap<>();

    public boolean isAllowed(String userId) {
        long now = Instant.now().toEpochMilli();

        userRequestLog.putIfAbsent(userId, new LinkedList<>());
        LinkedList<Long> requests = userRequestLog.get(userId);

        synchronized (requests) {
            // Remove old timestamps
            while (!requests.isEmpty() && (now - requests.peek()) > TIME_WINDOW_MILLIS) {
                requests.poll();
            }

            if (requests.size() < REQUEST_LIMIT) {
                requests.add(now);
                return true;
            }

            return false;
        }
    }
    */
}