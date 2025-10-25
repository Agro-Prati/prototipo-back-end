package com.agromaisprati.prototipobackagrospring.service.user.impl;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.ConflictException;
import com.agromaisprati.prototipobackagrospring.controller.exceptions.NotFoundException;
import com.agromaisprati.prototipobackagrospring.controller.mapper.user.UserMapper;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserUpdateDto;
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
    @Transactional(readOnly = true)
    public UserResponseDto findUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
            .map(UserMapper::toDto)
            .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserDto dto) {
        userValidator.hasEmail(dto.getEmail());
        User user = UserMapper.toEntity(dto, passwordEncoder);
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
        user.setType(dto.getType());
        user.setPhone(dto.getPhone());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setDescription(dto.getDescription());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserPartial(String id, UserUpdateDto dto) {
        User user = this.userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        
        // Verificar email apenas se for fornecido
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            Optional<User> userByEmail = this.userRepository.findByEmail(dto.getEmail());
            if (userByEmail.isPresent()) {
                boolean isSameUserWithEmail = userByEmail.get().getId().equals(user.getId());
                if (!isSameUserWithEmail) {
                    throw new ConflictException("Email is already registered.");
                }
            }
            user.setEmail(dto.getEmail());
        }
        
        // Atualizar apenas os campos fornecidos
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            user.setName(dto.getName());
        }
        
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        if (dto.getType() != null) {
            user.setType(dto.getType());
        }
        
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        
        if (dto.getCity() != null) {
            user.setCity(dto.getCity());
        }
        
        if (dto.getState() != null) {
            user.setState(dto.getState());
        }
        
        if (dto.getDescription() != null) {
            user.setDescription(dto.getDescription());
        }
        
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
