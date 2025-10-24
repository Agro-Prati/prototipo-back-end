package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.model.auth.LoginRequestDto;
import com.agromaisprati.prototipobackagrospring.model.auth.TokenResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private UserDto userDto;
    private LoginRequestDto loginRequest;

    @BeforeEach
    public void setup() {
        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setType(TipoUsuario.AGRICULTOR);
        userDto.setCity("Porto Alegre");
        userDto.setState("RS");

        loginRequest = new LoginRequestDto("test@example.com", "password123");
    }

    @Test
    public void registerShouldReturnCreatedWhenValidData() {
        TokenResponseDto tokenResponse = new TokenResponseDto("fake-jwt-token");
        when(authService.register(any(UserDto.class))).thenReturn(tokenResponse);

        ResponseEntity<TokenResponseDto> response = authController.register(userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isEqualTo("fake-jwt-token");
    }

    @Test
    public void loginShouldReturnTokenWhenValidCredentials() {
        TokenResponseDto tokenResponse = new TokenResponseDto("fake-jwt-token");
        when(authService.login(any(LoginRequestDto.class))).thenReturn(tokenResponse);

        ResponseEntity<TokenResponseDto> response = authController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isEqualTo("fake-jwt-token");
    }
}
