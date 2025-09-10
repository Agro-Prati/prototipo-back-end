package com.agromaisprati.prototipobackagrospring.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;

public interface UserService {

    Page<UserResponseDto> findAllUsers(Pageable pageable);
    UserResponseDto findUserById(String id);
    UserResponseDto createUser(UserDto dto);
    void updateUser(String id, UserDto dto);
    void deleteUser(String id);

}
