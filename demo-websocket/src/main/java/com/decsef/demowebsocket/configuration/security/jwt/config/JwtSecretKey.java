package com.decsef.demowebsocket.configuration.security.jwt.config;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 * Clase que funciona para configurar que se encripten los JWT'S en bytes.
 */
@Configuration
@RequiredArgsConstructor
public class JwtSecretKey {

    @Autowired
    private final JwtConfig jwtConfig;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

}
