package com.decsef.demowebsocket.student;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Iterable<Student> readAllStudents(){
        return studentRepository.findAll();
    }

    public Student readStudentById(Long id) throws NotFoundException {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User doesn't exist."));
    }

    public Student createStudent(Student student){
        return studentRepository.save(student);
    }

}
