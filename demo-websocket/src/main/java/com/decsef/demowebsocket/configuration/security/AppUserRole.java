package com.decsef.demowebsocket.configuration.security;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.decsef.demowebsocket.configuration.security.AppUserPermission.*;

/**
 * Clase que contiene los tipos de roles de usuarios para la aplicación.
 * Crea una colection con los permisos que contiene cada usuario.
 */
@Getter
public enum AppUserRole {
    ADMINISTRADOR(Sets.newHashSet(ROLE, MONITORING, SOURCE_CREATE_UPDATE_DELETE, SOURCE_READ)),
    OPERADOR(Sets.newHashSet(PRODUCT, SOURCE_READ));

    private final Set<AppUserPermission> permissions;

    AppUserRole(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

}
