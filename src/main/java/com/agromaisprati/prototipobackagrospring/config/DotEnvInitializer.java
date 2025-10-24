package com.agromaisprati.prototipobackagrospring.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuração para carregar variáveis do arquivo .env
 */
public class DotEnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
            
            Map<String, Object> envVars = dotenv.entries()
                    .stream()
                    .collect(Collectors.toMap(
                            entry -> entry.getKey(),
                            entry -> entry.getValue()
                    ));
            
            environment.getPropertySources()
                    .addFirst(new MapPropertySource("dotenv", envVars));
                    
        } catch (Exception e) {
            // Se não conseguir carregar o .env, continua sem ele
            System.out.println("Arquivo .env não encontrado ou com erro, usando variáveis de ambiente do sistema");
        }
    }
}