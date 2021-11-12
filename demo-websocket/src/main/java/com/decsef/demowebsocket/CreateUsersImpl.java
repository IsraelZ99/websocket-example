package com.decsef.demowebsocket;

import com.decsef.demowebsocket.student.Student;
import com.decsef.demowebsocket.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateUsersImpl implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        studentRepository.save(new Student("Israel", "Garcia", "israel@hotmail.com"));
    }
}
