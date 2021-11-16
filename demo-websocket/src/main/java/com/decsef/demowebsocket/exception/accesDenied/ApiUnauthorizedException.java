package com.decsef.demowebsocket.exception.accesDenied;

/**
 * Excepcion conforme a que el usuario no este autorizado.
 */
public class ApiUnauthorizedException extends RuntimeException{

    public ApiUnauthorizedException(String message) {
        super(message);
    }

    public ApiUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
