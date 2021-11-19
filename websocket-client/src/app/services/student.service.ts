import { Injectable } from '@angular/core';
import * as StompJs from 'stompjs';
import { Student } from 'app/models/Student';
import { first, map, Observable, Subject } from 'rxjs';
import { SocketClientService } from './socket-client.service';
import { HttpClient } from '@angular/common/http'
import { MessageEvent } from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private socketClient: SocketClientService, private http: HttpClient) {
  }

  public findAll(): Observable<Student[]> {
    return this.socketClient.onMessage("/students/get");
  }

  public retrieveStudentCreated(): Observable<Student> {
    return this.socketClient.onMessage("/students/created");
  }

  public retrieveStudentInformation(idStudent: number): Observable<Student> {
    return this.socketClient.onMessage(`/students/${idStudent}/get`);
  }

  public createStudent(student: Student) {
    this.socketClient.send("/students/create", student);
  }

}