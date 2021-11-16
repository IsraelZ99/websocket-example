package com.decsef.demowebsocket.user.repositories;

import com.decsef.demowebsocket.user.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
