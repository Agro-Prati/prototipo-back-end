package com.agromaisprati.prototipobackagrospring.service.auth;

import com.agromaisprati.prototipobackagrospring.model.auth.LoginRequestDto;
import com.agromaisprati.prototipobackagrospring.model.auth.TokenResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void register(UserDto user);
    TokenResponseDto login(LoginRequestDto login, HttpServletResponse response);
    TokenResponseDto refreshToken(HttpServletRequest request);
    void logout(HttpServletRequest request, HttpServletResponse response);

}
