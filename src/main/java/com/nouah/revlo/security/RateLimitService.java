package com.nouah.revlo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
//@RequiredArgsConstructor
public class RateLimitService {

    @Value("${ratelimit.limit}")
    private int maxRequests;

    private  StringRedisTemplate redisTemplate;

//
//    public RateLimiterService(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }


    @Value("${ratelimit.window-seconds}")
    private int windowInSeconds;

    public boolean isAllowed(String key) {
        String redisKey = "rate:" + key;
        Long current = redisTemplate.opsForValue().increment(redisKey);

        if (current == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(windowInSeconds));
        }

        return current <= maxRequests;
    }
}
