package com.decsef.demowebsocket.student;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public Iterable<Student> getAllStudents(){
        return studentService.readAllStudents();
    }

    @GetMapping("{id}")
    public Student getStudentById(@PathVariable Long id) throws NotFoundException {
        return studentService.readStudentById(id);
    }

}
