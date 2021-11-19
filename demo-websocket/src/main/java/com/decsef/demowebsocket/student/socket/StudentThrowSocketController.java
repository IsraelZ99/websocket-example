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

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class StudentThrowSocketController {

    private final StudentService studentService;

    @SubscribeMapping("/students/get")
    public List<Student> findAllStudents(){
        return studentService.readAllStudents();
    }

    @SubscribeMapping("/students/{id}/get")
    public Student findById(@DestinationVariable Long id, Principal principal) throws NotFoundException {
        return studentService.readStudentById(id);
    }

    @MessageMapping("/students/create")
    @SendTo("/students/created")
    public Student saveStudent(Student student){
        return studentService.createStudent(student);
    }

    @MessageExceptionHandler
    @SendToUser("/students/error")
    public String handleException(NotFoundException ex){
        log.info("Student not found");
        return "The requested student was not found";
    }

}
