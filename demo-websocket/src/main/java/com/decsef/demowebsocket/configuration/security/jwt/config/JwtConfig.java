package com.decsef.demowebsocket.configuration.security.jwt.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Clase que lee las propiedades application.jwt de el archivo application.yml.
 */
@ConfigurationProperties(prefix = "application.jwt")
@Configuration
@NoArgsConstructor
@Getter
@Setter
public class JwtConfig {

    /**
     * Contraseña para encriptar JWT'S al crearlos.
     */
    private String secretKey;
    /**
     * Prefijo que llevara la cabecera de authenticación.
     */
    private String tokenPrefix;
    /**
     * Día(s) en el que expirara un token.
     */
    private Integer tokenExpirationAfterDays;

    /**
     * Método para obtener la cabecera de authorización.
     * @return La cabecera.
     */
    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }

}
