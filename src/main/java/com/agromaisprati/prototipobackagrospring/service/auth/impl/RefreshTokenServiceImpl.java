package com.agromaisprati.prototipobackagrospring.service.auth.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.agromaisprati.prototipobackagrospring.constants.CacheKeys;
import com.agromaisprati.prototipobackagrospring.service.auth.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveRefreshToken(Jwt refreshToken) {
        this.redisTemplate.opsForValue().set(CacheKeys.REFRESH_TOKEN + ":" + refreshToken.getId(), refreshToken.getTokenValue());
    }

    @Override
    public void deleteRefreshToken(String refreshTokenId) {
        this.redisTemplate.delete(CacheKeys.REFRESH_TOKEN + ":" + refreshTokenId);
    }

    @Override
    public boolean refreshTokenExists(String refreshTokenId) {
        return this.redisTemplate.hasKey(CacheKeys.REFRESH_TOKEN + ":" + refreshTokenId);
    }

    

}
