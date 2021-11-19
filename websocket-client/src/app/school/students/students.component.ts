import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Student } from 'app/models/Student';
import { StudentService } from 'app/services/student.service';
import { map, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.scss']
})
export class StudentsComponent implements OnInit, OnDestroy {

  public students: Student[];
  public studentForm: FormGroup;
  private unsubscribeSubject: Subject<void>;

  constructor(private studentService: StudentService) {
    this.students = [];
    this.unsubscribeSubject = new Subject<void>();
    this.studentForm = new FormGroup(
      {
        name: new FormControl('', [Validators.required]),
        lastName: new FormControl('', [Validators.required]),
        email: new FormControl('', [Validators.required])
      }
    )
  }

  public ngOnInit(): void {
    this.studentService.findAll()
      .pipe(map(students => students.sort((student1, student2) => (student1.name > student2.name) ? 1 : ((student2.name > student1.name) ? -1 : 0))),
        takeUntil(this.unsubscribeSubject))
      .subscribe(students => this.students = students);

    this.studentService.retrieveStudentCreated()
      .pipe(takeUntil(this.unsubscribeSubject))
      .subscribe(student => this.students.push(student));
  }

  public ngOnDestroy(): void {
    this.unsubscribeSubject.next();
    this.unsubscribeSubject.complete();
  }

  public saveStudent() {
    this.studentService.createStudent(this.studentForm.value);
    this.studentForm.reset();
  }

  public editUser(studentId: number) {
    this.studentService.retrieveStudentInformation(studentId)
      .subscribe(student => {
        this.studentForm.value.name = student.name;
        this.studentForm.value.lastName = student.lastName;
        this.studentForm.value.email = student.email;
      });
  }

}
