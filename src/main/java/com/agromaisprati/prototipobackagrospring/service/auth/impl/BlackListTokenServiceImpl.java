package com.agromaisprati.prototipobackagrospring.service.auth.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.agromaisprati.prototipobackagrospring.constants.CacheKeys;
import com.agromaisprati.prototipobackagrospring.service.auth.BlackListTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlackListTokenServiceImpl implements BlackListTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void addTokenToBlackList(Jwt accessToken) {
        this.redisTemplate.opsForValue().set(CacheKeys.BLACKLIST_JWT + ":" + accessToken.getId(), accessToken.getTokenValue());
    }

    @Override
    public boolean isTokenBlackListed(String accessTokenId) {
        return this.redisTemplate.hasKey(CacheKeys.BLACKLIST_JWT + ":" + accessTokenId);
    }

}
