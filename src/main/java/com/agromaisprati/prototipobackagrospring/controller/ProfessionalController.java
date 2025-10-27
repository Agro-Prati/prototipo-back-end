package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para busca de profissionais por especialidade
 * Usado para integração com o chatbot e networking entre usuários
 */
@RestController
@RequestMapping("/api/professionals")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:5000"})
public class ProfessionalController {

    private final UserService userService;

    /**
     * Busca profissionais por especialidade
     * 
     * @param specialty Tipo de profissional (AGRONOMO, VETERINARIO, ZOOTECNISTA)
     * @param limit Número máximo de resultados (padrão: 3)
     * @return Lista de profissionais encontrados
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchProfessionals(
            @RequestParam String specialty,
            @RequestParam(defaultValue = "3") int limit) {
        
        try {
            TipoUsuario tipoUsuario = TipoUsuario.valueOf(specialty.toUpperCase());
            List<UserResponseDto> professionals = userService.findProfessionalsByType(tipoUsuario, limit);
            return ResponseEntity.ok(professionals);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca profissionais por múltiplas especialidades
     * 
     * @param specialties Lista de especialidades separadas por vírgula
     * @param limit Número máximo de resultados por especialidade
     * @return Lista de profissionais encontrados
     */
    @GetMapping("/search-multiple")
    public ResponseEntity<List<UserResponseDto>> searchMultipleProfessionals(
            @RequestParam String specialties,
            @RequestParam(defaultValue = "2") int limit) {
        
        try {
            String[] specialtyArray = specialties.split(",");
            List<UserResponseDto> professionals = userService.findProfessionalsByTypes(specialtyArray, limit);
            return ResponseEntity.ok(professionals);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
