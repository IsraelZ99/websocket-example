package com.decsef.demowebsocket.configuration.socket;

import com.decsef.demowebsocket.configuration.auth.ApplicationUserService;
import com.decsef.demowebsocket.configuration.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@ConfigurationProperties
@EnableConfigurationProperties(WebSocketProperties.class)
@AllArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private WebSocketProperties properties;
    private final JwtUtils jwtUtils;
    private final ApplicationUserService applicationUserService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(properties.getApplicationPrefix());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(properties.getEndpoint())
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new SocketHandshake(jwtUtils, applicationUserService))
                .addInterceptors(new SocketHandshake(jwtUtils, applicationUserService))
                .withSockJS();
    }
}
