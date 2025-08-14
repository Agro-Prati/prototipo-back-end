package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.controller.mapper.user.UserMapper;
import com.agromaisprati.prototipobackagrospring.model.user.UserDTO;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class UserController implements GenericController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/public/auth/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO response) {
        return ResponseEntity.created(generatorDefaultHeaderLocation(userService.registerUser(userMapper.toEntity(response)).getId())).build();
    }

}
