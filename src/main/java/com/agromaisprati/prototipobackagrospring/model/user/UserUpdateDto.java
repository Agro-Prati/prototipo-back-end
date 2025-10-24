package com.agromaisprati.prototipobackagrospring.model.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização parcial de usuários
 * Todos os campos são opcionais para permitir atualizações parciais
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String name;

    @Email(message = "Email inválido")
    private String email;

    private String password;

    private TipoUsuario type;

    private String phone;
    
    private String city;
    
    private String state;
    
    private String description;
}