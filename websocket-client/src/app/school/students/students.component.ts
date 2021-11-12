import { Component, OnInit } from '@angular/core';
import { Student } from 'app/models/Student';
import { StudentService } from 'app/services/student.service';
import { map } from 'rxjs';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.scss']
})
export class StudentsComponent implements OnInit {

  public students: Student[];

  constructor(private studentService: StudentService) {
    this.students = [];
   }

  ngOnInit(): void {
    this.studentService.requestFindAll().subscribe(sutudents => console.log(sutudents));
    
    // this.studentService.findAll()
    //   .pipe(map(students => students.sort((student1, student2) => (student1.name > student2.name) ? 1 : ((student2.name > student1.name) ? -1 : 0))))
    //   .subscribe(students => this.students = students);
  }

}
