package com.agromaisprati.prototipobackagrospring.validator.user;

import com.agromaisprati.prototipobackagrospring.controller.exceptions.ConflictException;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void hasEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email is already registered.");
        }
    }

}
