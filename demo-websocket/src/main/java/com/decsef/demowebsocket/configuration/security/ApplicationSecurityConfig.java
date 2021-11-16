package com.decsef.demowebsocket.configuration.security;

import com.decsef.demowebsocket.configuration.auth.ApplicationUserService;
import com.decsef.demowebsocket.configuration.security.jwt.AuthEntryPointJwt;
import com.decsef.demowebsocket.configuration.security.jwt.AuthTokenFilter;
import com.decsef.demowebsocket.configuration.security.jwt.JwtUtils;
import com.decsef.demowebsocket.configuration.security.jwt.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;

/**
 * Clase que contiene la configuración de Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    /**
     * Tipo de encriptación/desencriptación para las contraseñas por usuario.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Contraseña para crear JWT'S.
     */
    private final SecretKey secretKey;
    /**
     * Clase que contiene la configuración para JWT'S.
     */
    private final JwtConfig jwtConfig;
    /**
     * Clase que contiene método para cargar la información de el usuario que intentará loguearse.
     */
    private final ApplicationUserService applicationUserService;
    /**
     * Clase que muestra por consola algún posible error referente a la autenticación de un usuario.
     */
    private final AuthEntryPointJwt unauthorizedHandler;
    /**
     * Clase que contiene utilidades/herramientas sobre JWT'S
     */
    private final JwtUtils jwtUtils;
    /**
     * Lista de rutas que seran permitidas, sin la necesidad de que el usuario se autentique.
     */
    private final String[] AUTH_WHITELIST = {
            "/*", "index", "/css/*", "/js/*", "/auth/**", "/assets/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/api/socket/**",
            "/login"
    };

    /**
     * Método para compartir el manejador, con algun otro componente que lo necesite.
     * @return El manejador de autenticación, para que se pueda acceder desde algun otro componente.
     * @throws Exception Previene un posible el error al crear el Bean.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Método que proviene de la clase WebSecurityConfigurerAdapter la cuál tiene el proposito de configurar las
     * entradas de peticiones
     * @param auth Clase que contiene metodos para generar la clase de autenticación.
     * @throws Exception Previene un posible el error al crear el Bean.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * Método para configurar el tipo de encriptación de contraseñas, y la busqueda de un usuario.
     * @return un proveedor de autenticación para configurar el tipo de encirptacion de contraseñas, y el servicio
     * que contiene el método para saber si existe o no, un usuario.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    /**
     * Método que configura lo referente a las peticiones htpp hechas a el backend.
     * @param http Clase que contiene método referente a la securidad de peticiones Http a el backend.
     * @throws Exception Previene un posible el error al crear el Bean.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .addFilterBefore(new AuthTokenFilter(secretKey, jwtConfig, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET","POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true)
                        .exposedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowedHeaders("*");
            }
        };

    }

}
