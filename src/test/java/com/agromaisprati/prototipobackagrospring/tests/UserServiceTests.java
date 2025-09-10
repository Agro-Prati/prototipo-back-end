package com.agromaisprati.prototipobackagrospring.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.ConflictException;
import com.agromaisprati.prototipobackagrospring.controller.exceptions.NotFoundException;
import com.agromaisprati.prototipobackagrospring.factories.UserFactory;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.repository.role.RoleRepository;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import com.agromaisprati.prototipobackagrospring.service.user.impl.UserServiceImpl;
import com.agromaisprati.prototipobackagrospring.validator.user.UserValidator;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    private User user;
    private UserDto userDto;
    private String existingId;
    private String nonExistingId;

    @BeforeEach
    public void setup() {
        this.user = UserFactory.createUser();
        this.userDto = UserFactory.createUserDto();
        this.existingId = this.user.getId().toString();
        this.nonExistingId = UUID.randomUUID().toString();
    }

    @Test
    public void findAllShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<UserResponseDto> result = userService.findAllUsers(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(user.getId().toString());
        assertThat(result.getContent().get(0).name()).isEqualTo(user.getName());
        assertThat(result.getContent().get(0).email()).isEqualTo(user.getEmail());
    }

    @Test
    public void findByIdShouldReturnUserResponseDtoWhenUserExists() {
        when(userRepository.findById(UUID.fromString(existingId))).thenReturn(Optional.of(user));

        UserResponseDto response = userService.findUserById(existingId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(existingId);
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.email()).isEqualTo(user.getEmail());
    }

    @Test
    public void findByIdShouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(UUID.fromString(nonExistingId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserById(nonExistingId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void createUserShouldReturnUserResponseDto() {
        doNothing().when(userValidator).hasEmail(anyString());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto response = userService.createUser(userDto);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(user.getId().toString());
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.email()).isEqualTo(user.getEmail());
    }

    @Test
    public void updateUserShouldThrowNoExceptionWhenUserExists() {
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findById(UUID.fromString(existingId))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThatCode(() -> userService.updateUser(existingId, userDto)).doesNotThrowAnyException();
    }

    @Test
    public void updateUserShouldThrowNoExceptionWhenUserExistsWhenIsSameUserWithEmail() {
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(UUID.fromString(existingId))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThatCode(() -> userService.updateUser(existingId, userDto)).doesNotThrowAnyException();
    }

    @Test
    public void updateUserShouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findById(UUID.fromString(nonExistingId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(nonExistingId, userDto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void updateUserShouldThrowConflictExceptionWhenUserExistsWhenIsNotSameUserWithEmail() {
        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());
        anotherUser.setEmail(userDto.getEmail());
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(anotherUser));
        when(userRepository.findById(UUID.fromString(existingId))).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updateUser(existingId, userDto)).isInstanceOf(ConflictException.class);
    }

    @Test
    public void deleteUserShouldThrowNoExceptionWhenUserExists() {
        when(userRepository.existsById(UUID.fromString(existingId))).thenReturn(true);
        doNothing().when(userRepository).deleteById(UUID.fromString(existingId));

        assertThatCode(() -> userService.deleteUser(existingId)).doesNotThrowAnyException();
    }

    @Test
    public void deleteUserShouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.existsById(UUID.fromString(nonExistingId))).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(nonExistingId)).isInstanceOf(NotFoundException.class);
    }

}
