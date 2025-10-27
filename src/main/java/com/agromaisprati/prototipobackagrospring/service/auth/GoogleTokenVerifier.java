package com.agromaisprati.prototipobackagrospring.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Serviço para validar tokens do Google Sign-In
 */
@Service
public class GoogleTokenVerifier {

    @Value("${google.client-id}")
    private String googleClientId;

    /**
     * Valida o token do Google e retorna o payload com os dados do usuário
     */
    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        try {
            System.out.println("Google Client ID configurado: " + googleClientId);
            System.out.println("Token recebido: " + idTokenString.substring(0, Math.min(50, idTokenString.length())) + "...");
            
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
            )
            .setAudience(Collections.singletonList(googleClientId))
            .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new RuntimeException("Token do Google inválido");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar token do Google: " + e.getMessage(), e);
        }
    }
}
