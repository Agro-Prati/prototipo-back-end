package com.agromaisprati.prototipobackagrospring.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserUpdateDto;

public interface UserService {

    Page<UserResponseDto> findAllUsers(Pageable pageable);
    UserResponseDto findUserById(String id);
    UserResponseDto findUserByEmail(String email);
    UserResponseDto createUser(UserDto dto);
    void updateUser(String id, UserDto dto);
    void updateUserPartial(String id, UserUpdateDto dto);
    void deleteUser(String id);

}
