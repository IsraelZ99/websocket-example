package com.decsef.demowebsocket.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase para configurar el tipo/tamaño de encriptado de las contraseñas.
 */
@Configuration
public class PasswordConfig {

    /**
     * Configuración de encriptacion de contraseñas.
     * @return La fuerza con la que se encriptan las contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
