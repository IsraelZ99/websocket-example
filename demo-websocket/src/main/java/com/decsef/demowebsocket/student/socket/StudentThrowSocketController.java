package com.decsef.demowebsocket.student.socket;

import com.decsef.demowebsocket.student.Student;
import com.decsef.demowebsocket.student.StudentService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
@Slf4j
public class StudentThrowSocketController {

    private final StudentService studentService;

    @SubscribeMapping("/students/get")
    public Iterable<Student> findAllStudents(){
        return studentService.readAllStudents();
    }

    @SubscribeMapping("/students/{id}/get")
    public Student findById(@DestinationVariable Long id) throws NotFoundException {
        return studentService.readStudentById(id);
    }

    @MessageMapping("/posts/create")
    @SendTo("/school/students/created")
    public void saveStudent(Student student){
        studentService.createStudent(student);
    }

    @MessageExceptionHandler
    @SendToUser("/school/error")
    public String handleException(){
        return "The requested post was not found";
    }

}
