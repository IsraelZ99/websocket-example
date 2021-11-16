package com.decsef.demowebsocket.configuration.auth;

import com.decsef.demowebsocket.user.entities.User;
import com.decsef.demowebsocket.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio que contiene método para saber si el usuario que intenta loguearse existe o no en el sistema.
 */
@Service
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with user: " + username));
        return
                new ApplicationUser(user.getUsername(), user.getPassword(),
                        user.getRol().getRol().getGrantedAuthorities());
    }

}
