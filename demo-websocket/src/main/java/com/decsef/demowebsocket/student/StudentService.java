package com.decsef.demowebsocket.student;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public List<Student> readAllStudents(){
        return studentRepository.findAll();
    }

    public Student readStudentById(Long id) throws NotFoundException {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User doesn't exist."));
    }

    public Student createStudent(Student student){
        return studentRepository.save(student);
//        simpMessagingTemplate.convertAndSend("/students/created", studentRepository.save(student));
    }

}
