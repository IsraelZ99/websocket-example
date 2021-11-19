import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SocketClientService } from './socket-client.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  public constructor(private socketClient: SocketClientService) { }

  public onError(): Observable<string> {
    return this.socketClient.onPlainMessage("/user/students/error");
  }

}
