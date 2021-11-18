package com.decsef.demowebsocket.configuration.socket;

import com.decsef.demowebsocket.configuration.auth.ApplicationUser;
import com.decsef.demowebsocket.configuration.auth.ApplicationUserService;
import com.decsef.demowebsocket.configuration.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

/**
 * HandshakeHandler that checks the token param before handshake.
 * If it's a valid token, the handshake will be done. If it isn't a valid token, the handshake will
 * be rejected and response will have HttpStatus.UNAUTHORIZED.
 */
@Slf4j
@RequiredArgsConstructor
public class SocketHandshake extends DefaultHandshakeHandler implements HandshakeInterceptor {

    /**
     * Clase que contiene utilidades/herramientas sobre JWT'S.
     */
    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    private final ApplicationUserService applicationUserService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Object token = attributes.get("token");
        String username = jwtUtils.getUserNameFromJwtToken((String) token);
        UserDetails userDetails = applicationUserService.loadUserByUsername(username);
        log.info("Username connected: " + userDetails.getUsername());
        return (Principal) userDetails;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = getTokenFromUri(request.getURI());
        if (jwtUtils.validateJwtToken(token)) {
            attributes.put("token", token);
            return true;
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        String token = getTokenFromUri(request.getURI());
        if (!jwtUtils.validateJwtToken(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
        }
    }

    public String getTokenFromUri(URI uri) {
        String token = "";
        String[] params = uri.getQuery().trim().split("&");
        for (String param : params) {
            String[] paramData = param.split("=");
            if (paramData.length > 1 && paramData[0].equals("token")) {
                token = paramData[1];
                break;
            }
        }
        return token;
    }
}
