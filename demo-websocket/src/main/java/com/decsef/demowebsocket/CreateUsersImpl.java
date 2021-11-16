package com.decsef.demowebsocket;

import com.decsef.demowebsocket.configuration.security.AppUserRole;
import com.decsef.demowebsocket.configuration.security.jwt.JwtUtils;
import com.decsef.demowebsocket.student.Student;
import com.decsef.demowebsocket.student.StudentRepository;
import com.decsef.demowebsocket.user.entities.Rol;
import com.decsef.demowebsocket.user.entities.User;
import com.decsef.demowebsocket.user.repositories.RolRepository;
import com.decsef.demowebsocket.user.repositories.UserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateUsersImpl implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final String password;

    public CreateUsersImpl() {
        this.password = "123456";
    }

    @Override
    public void run(String... args) throws Exception {
        Student student = studentRepository.save(new Student("Israel", "Garcia", "israel@hotmail.com"));
        log.info("Student: "+ student);
        Rol rol = rolRepository.save(new Rol(AppUserRole.ADMINISTRADOR));
        log.info("Rol: "+ rol);
        User user = userRepository.save(new User("israel.garcia", passwordEncoder.encode(password),
                "Israel Garcia",
                rolRepository.findById(2L).orElseThrow(() -> new NotFoundException("Rol not found"))
        ));
        log.info("User: "+ user);

        generateToken(user);
    }

    private void generateToken(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), password);
        Authentication authenticate = authenticationManager.authenticate(authentication);
        String token = jwtUtils.generateJwtToken(authenticate);
        log.info("Token: "+ token);
    }
}
