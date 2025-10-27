package com.agromaisprati.prototipobackagrospring.config;

import com.agromaisprati.prototipobackagrospring.model.user.TipoUsuario;
import com.agromaisprati.prototipobackagrospring.model.user.User;
import com.agromaisprati.prototipobackagrospring.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializa usuários de exemplo para cada tipo de profissão
 * ATENÇÃO: Este componente só é ativado no profile 'dev' para desenvolvimento/testes
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Criando usuários de exemplo...");
            
            createUser("Agricultor Silva", "agricultor@agro.com", "senha123", 
                      TipoUsuario.AGRICULTOR, "Porto Alegre", "RS", 
                      "Agricultor familiar com 20 anos de experiência");
            
            createUser("Dr. João Agrônomo", "agronomo@agro.com", "senha123", 
                      TipoUsuario.AGRONOMO, "São Paulo", "SP", 
                      "Engenheiro Agrônomo especialista em culturas perenes");
            
            createUser("Dra. Maria Veterinária", "veterinaria@agro.com", "senha123", 
                      TipoUsuario.VETERINARIO, "Curitiba", "PR", 
                      "Médica Veterinária especializada em grandes animais");
            
            createUser("Carlos Zootecnista", "zootecnista@agro.com", "senha123", 
                      TipoUsuario.ZOOTECNISTA, "Goiânia", "GO", 
                      "Zootecnista com foco em nutrição animal");
            
            createUser("Ana Estudante", "estudante@agro.com", "senha123", 
                      TipoUsuario.ESTUDANTE, "Brasília", "DF", 
                      "Estudante de Agronomia - 3º ano");
            
            log.info("Usuários de exemplo criados com sucesso!");
            log.info("Todos os usuários usam a senha: senha123");
        } else {
            log.info("Banco de dados já contém usuários. Pulando inicialização.");
        }
    }

    private void createUser(String name, String email, String password, 
                           TipoUsuario type, String city, String state, String description) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setType(type);
        user.setCity(city);
        user.setState(state);
        user.setDescription(description);
        user.setPhone("(00) 00000-0000");
        
        userRepository.save(user);
        log.info("Criado usuário: {} - {} ({})", name, email, type);
    }
}
