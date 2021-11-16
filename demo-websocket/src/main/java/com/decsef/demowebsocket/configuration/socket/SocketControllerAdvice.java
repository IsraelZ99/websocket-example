package com.decsef.demowebsocket.configuration.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.socket.adapter.standard.StandardWebSocketHandlerAdapter;
import org.springframework.web.socket.sockjs.SockJsMessageDeliveryException;

@Slf4j
@Controller
@ControllerAdvice
public class SocketControllerAdvice {

  @MessageExceptionHandler
  public void handleException(SockJsMessageDeliveryException e) {
    StandardWebSocketHandlerAdapter s;
  }
}
