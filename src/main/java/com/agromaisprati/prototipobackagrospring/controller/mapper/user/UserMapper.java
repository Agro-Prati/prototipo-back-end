package com.agromaisprati.prototipobackagrospring.controller.mapper.user;

import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user);
    }

    public static User toEntity(UserDto dto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setType(dto.getType());
        user.setPhone(dto.getPhone());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setDescription(dto.getDescription());
        return user;
    }
}
