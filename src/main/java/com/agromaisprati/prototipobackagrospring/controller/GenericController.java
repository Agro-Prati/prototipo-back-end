package com.agromaisprati.prototipobackagrospring.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {
    default URI generatorDefaultHeaderLocation(UUID id){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/user/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
