package com.decsef.demowebsocket.configuration.security;

import lombok.Getter;

/**
 * Clase que contiene los permisos para acceder a la aplicacion.
 */
@Getter
public enum AppUserPermission {
    ROLE("role:crud"),
    MONITORING("user:monitoring"),
    PRODUCT("product:crud"),
    SOURCE_READ("source:read"),
    SOURCE_CREATE_UPDATE_DELETE("source:create-update-delete");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }
}
