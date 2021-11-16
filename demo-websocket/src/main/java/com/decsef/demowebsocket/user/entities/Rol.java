package com.decsef.demowebsocket.user.entities;

import com.decsef.demowebsocket.configuration.security.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Data
@NoArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AppUserRole rol;

    public Rol(AppUserRole rol) {
        this.rol = rol;
    }
}
