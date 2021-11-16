package com.decsef.demowebsocket.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.websocket")
public class WebSocketProperties {

    /**
     * Prefix used for WebSocket destination mappings
     */
    private String[] applicationPrefix = {"/school"};
    /**
     * Endpoint that can be used to connect to
     */
    private String endpoint = "/api/socket";
    /**
     * Allowed origins
     */
    private String[] allowedOrigins = new String[0];

}
