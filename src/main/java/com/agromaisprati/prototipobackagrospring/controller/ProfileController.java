package com.agromaisprati.prototipobackagrospring.controller;

import com.agromaisprati.prototipobackagrospring.model.user.UserResponseDto;
import com.agromaisprati.prototipobackagrospring.model.user.UserUpdateDto;
import com.agromaisprati.prototipobackagrospring.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para o usuário gerenciar seu próprio perfil
 * O ID do usuário é extraído automaticamente do token JWT
 */
@Tag(name = "Meu Perfil", description = "Gerenciamento do perfil do usuário autenticado")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final UserService userService;

    @Operation(
        summary = "Ver meu perfil", 
        description = "Retorna os dados do perfil do usuário autenticado (extraído do token JWT)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
    })
    @GetMapping
    public ResponseEntity<UserResponseDto> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserResponseDto user = userService.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Atualizar meu perfil", 
        description = "Atualiza os dados do perfil do usuário autenticado. Apenas os campos fornecidos serão atualizados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
    })
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserUpdateDto dto) {
        String email = userDetails.getUsername();
        UserResponseDto user = userService.findUserByEmail(email);
        userService.updateUserPartial(user.id(), dto);
        
        // Retorna o perfil atualizado
        UserResponseDto updatedUser = userService.findUserByEmail(email);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
        summary = "Deletar minha conta", 
        description = "Remove permanentemente a conta do usuário autenticado. Esta ação é irreversível."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserResponseDto user = userService.findUserByEmail(email);
        userService.deleteUser(user.id());
        return ResponseEntity.noContent().build();
    }
}
