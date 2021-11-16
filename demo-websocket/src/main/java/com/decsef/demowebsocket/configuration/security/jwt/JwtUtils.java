package com.decsef.demowebsocket.configuration.security.jwt;

import com.decsef.demowebsocket.configuration.security.jwt.config.JwtConfig;
import com.decsef.demowebsocket.exception.accesDenied.ApiUnauthorizedException;
import com.decsef.demowebsocket.user.entities.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

/**
 * Clase que contiene herramientas de JWT'S.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtils {

    /**
     * Clase que contiene la configuración para JWT'S.
     */
    private final JwtConfig jwtConfig;
    /**
     * Contraseña para crear JWT'S.
     */
    private final SecretKey secretKey;

    /**
     * Método para generar un token.
     *
     * @param authentication Datos de el usuario que se ah autenticado de manera correcta.
     * @return La creacion de un nuevo JWT conforme a el nombre de el usuario, sus permisos, fecha de creación,
     * fecha de expiración.
     */
    public String generateJwtToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Método para generar un token para un usuario en especifíco.
     *
     * @param user Datos de el usuario.
     * @return La creacion de un nuevo JWT conforme a el nombre de el usuario, sus permisos, fecha de creación,
     * fecha de expiración.
     */
    public String generateJwtTokenFromUsername(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", user.getRol().getRol().getGrantedAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Método para obtener el nombre de el usuario conforme a un token.
     *
     * @param token Token asociado a un usario.
     * @return EL nombre de el usuario logueado.
     */
    public String getUserNameFromJwtToken(String token) {
        boolean isValidJwtToken = validateJwtToken(token);
        if (!isValidJwtToken) {
            throw new ApiUnauthorizedException("No has iniciado sesión aún.");
        }
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Método que funciona para validar un token.
     *
     * @param authToken Token.
     * @return Si el token esta de forma correcta/incorrecta.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
