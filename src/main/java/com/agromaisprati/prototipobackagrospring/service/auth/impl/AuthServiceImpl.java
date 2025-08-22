package com.agromaisprati.prototipobackagrospring.service.auth.impl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.UnauthorizedException;
import com.agromaisprati.prototipobackagrospring.model.auth.LoginRequestDto;
import com.agromaisprati.prototipobackagrospring.model.auth.TokenResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDTO;
import com.agromaisprati.prototipobackagrospring.repository.role.RoleRepository;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import com.agromaisprati.prototipobackagrospring.service.auth.AuthService;
import com.agromaisprati.prototipobackagrospring.service.auth.BlackListTokenService;
import com.agromaisprati.prototipobackagrospring.service.auth.JwtService;
import com.agromaisprati.prototipobackagrospring.service.auth.RefreshTokenService;
import com.agromaisprati.prototipobackagrospring.validator.user.UserValidator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final BlackListTokenService blackListTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;

    @Value("${jwt.refresh-token.duration}")
    private int refreshTokenDuration;
    @Value("${jwt.cookie.secure}")
    private boolean isCookieSecure;

    @Override
    public void register(UserDTO dto) {
        userValidator.hasEmail(dto.getEmail());
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto login, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
        );
        Jwt accessToken = jwtService.encodeAccessToken(authentication);
        Jwt refreshToken = jwtService.encodeRefreshToken(authentication);
        refreshTokenService.saveRefreshToken(refreshToken);
        setRefreshTokenOnCookie(response, refreshToken.getTokenValue());
        return new TokenResponseDto(accessToken.getTokenValue());
    }

    @Override
    public TokenResponseDto refreshToken(HttpServletRequest request) {
        String refreshToken = obtainRefreshTokenFromCookie(request);
        Jwt decodedRefreshToken = jwtService.decodeJwt(refreshToken);
        if (!this.refreshTokenService.refreshTokenExists(decodedRefreshToken.getId())) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        User user = userRepository.findByEmail(decodedRefreshToken.getSubject())
            .orElseThrow(() -> new UnauthorizedException("User not found"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        Jwt accessToken = jwtService.encodeAccessToken(authentication);
        return new TokenResponseDto(accessToken.getTokenValue());
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = obtainRefreshTokenFromCookie(request);
        Jwt decodedRefreshToken = jwtService.decodeJwt(refreshToken);
        refreshTokenService.deleteRefreshToken(decodedRefreshToken.getId());
        setRefreshTokenOnCookie(response, "");
        String bearerToken = request.getHeader("Authorization");
        Jwt decodedAccessToken = jwtService.decodeJwt(bearerToken.split(" ")[1]);
        blackListTokenService.addTokenToBlackList(decodedAccessToken);
    }

    private void setRefreshTokenOnCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
            .path("/")
            .maxAge(Duration.ofDays(refreshTokenDuration).minusHours(3L))
            .sameSite("None")
            .httpOnly(true)
            .secure(isCookieSecure)
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private String obtainRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnauthorizedException("No refresh cookie");
        }
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
                break;
            }
        }
        if (refreshToken == null) {
            throw new UnauthorizedException("No refresh cookie");
        }
        return refreshToken;
    }

}
