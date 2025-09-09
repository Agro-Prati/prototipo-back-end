package com.agromaisprati.prototipobackagrospring.service.user.impl;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.ConflictException;
import com.agromaisprati.prototipobackagrospring.controller.exceptions.NotFoundException;
import com.agromaisprati.prototipobackagrospring.controller.mapper.user.UserMapper;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.repository.role.RoleRepository;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;
import com.agromaisprati.prototipobackagrospring.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAllUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable).map(UserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(String id) {
        return this.userRepository.findById(UUID.fromString(id))
            .map(UserMapper::toDto)
            .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserDto dto) {
        userValidator.hasEmail(dto.getEmail());
        User user = UserMapper.toEntity(dto, passwordEncoder);
        user.setRole(roleRepository.findByName("ROLE_USER"));
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void updateUser(String id, UserDto dto) {
        Optional<User> userByEmail = this.userRepository.findByEmail(dto.getEmail());
        User user = this.userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        if (userByEmail.isPresent()) {
            boolean isSameUserWithEmail = userByEmail.get().getId().equals(user.getId());
            if (!isSameUserWithEmail) {
                throw new ConflictException("Email is already registered.");
            }
        }
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteUser(String id) {
        if (!this.userRepository.existsById(UUID.fromString(id))) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        this.userRepository.deleteById(UUID.fromString(id));
    }

}
