import { Injectable } from '@angular/core';
import { Student } from 'app/models/Student';
import { first, map, Observable } from 'rxjs';
import { SocketClientService } from './socket-client.service';
import { HttpClient } from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private socketClient: SocketClientService, private http: HttpClient) { }

  public findAll(): Observable<Student[]> {
    return this.socketClient.onMessage("/school/students/get")
      .pipe(first(), map(students => students.map()))
  }

  public getStudentsListing(student: any){
    return { ...student };
  }

  public requestFindAll(): Observable<any>{
    return this.http.get('http://localhost:8080/api/student');
  }

}