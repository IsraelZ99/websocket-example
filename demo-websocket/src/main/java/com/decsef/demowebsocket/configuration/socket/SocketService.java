package com.decsef.demowebsocket.configuration.socket;

import com.decsef.demowebsocket.configuration.auth.ApplicationUser;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SocketService implements ApplicationListener<AbstractSubProtocolEvent> {

    private final Map<String, ApplicationUser> delegate = new ConcurrentHashMap<>();
    private final List<Consumer<ApplicationUser>> onConnectConsumers = new CopyOnWriteArrayList<>();
    private final List<Consumer<ApplicationUser>> onDisconnectConsumers = new CopyOnWriteArrayList<>();

    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {

    }
}
