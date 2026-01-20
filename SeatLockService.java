package com.example.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SeatLockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final long LOCK_TIMEOUT = 300;

    public boolean lockSeat(Long showId, Long seatId, Long userId) {
        String key = buildKey(showId, seatId);
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(key, userId.toString(), LOCK_TIMEOUT, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(locked);
    }

    public void releaseSeat(Long showId, Long seatId) {
        redisTemplate.delete(buildKey(showId, seatId));
    }

    private String buildKey(Long showId, Long seatId) {
        return "seat-lock:" + showId + ":" + seatId;
    }
}