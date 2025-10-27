package com.agromaisprati.prototipobackagrospring.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.UserDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserUpdateDto;

import java.util.List;

public interface UserService {

    Page<UserResponseDto> findAllUsers(Pageable pageable);
    UserResponseDto findUserById(String id);
    UserResponseDto findUserByEmail(String email);
    UserResponseDto createUser(UserDto dto);
    void updateUser(String id, UserDto dto);
    void updateUserPartial(String id, UserUpdateDto dto);
    void deleteUser(String id);
    
    // Métodos para busca de profissionais (networking)
    List<UserResponseDto> findProfessionalsByType(TipoUsuario type, int limit);
    List<UserResponseDto> findProfessionalsByTypes(String[] types, int limit);

}
