package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.model.user.AuthProvider;
import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserUpdateDto;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDto userDto;
    private UserResponseDto userResponse;
    private String userId;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID().toString();
        
        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setType(TipoUsuario.AGRICULTOR);
        userDto.setCity("Porto Alegre");
        userDto.setState("RS");

        userResponse = new UserResponseDto(
                userId,
                "Test User",
                "test@example.com",
                TipoUsuario.AGRICULTOR,
                "Porto Alegre",
                "RS",
                "Descrição do usuário de teste",
                "(51) 99999-9999",
                AuthProvider.LOCAL,
                null,
                null,
                false,
                LocalDateTime.now()
        );
    }

    @Test
    public void findAllUsersShouldReturnPageOfUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponseDto> page = new PageImpl<>(List.of(userResponse), pageable, 1);
        when(userService.findAllUsers(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<UserResponseDto>> response = userController.findAllUsers(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0).name()).isEqualTo("Test User");
    }

    @Test
    public void findUserByIdShouldReturnUser() {
        when(userService.findUserById(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponseDto> response = userController.findUserById(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(userId);
        assertThat(response.getBody().name()).isEqualTo("Test User");
    }

    @Test
    public void updateUserShouldReturnOk() {
        doNothing().when(userService).updateUser(anyString(), any(UserDto.class));

        ResponseEntity<Void> response = userController.updateUser(userId, userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteUserShouldReturnNoContent() {
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void updateUserPartialShouldReturnOk() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Nome Atualizado");
        
        doNothing().when(userService).updateUserPartial(anyString(), any(UserUpdateDto.class));

        ResponseEntity<Void> response = userController.updateUserPartial(userId, userUpdateDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
