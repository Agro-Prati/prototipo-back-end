package com.agromaisprati.prototipobackagrospring.validator.user;

import com.agromaisprati.prototipobackagrospring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    @SneakyThrows
    public void hasEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("");
        }
    }

}
