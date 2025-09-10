package com.agromaisprati.prototipobackagrospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PrototipoBackAgroSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrototipoBackAgroSpringApplication.class, args);
    }

}
