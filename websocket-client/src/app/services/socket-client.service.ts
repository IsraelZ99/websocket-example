import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import * as StompJs from 'stompjs';
import { Observable } from 'rxjs/internal/Observable';
import { filter, first, switchMap } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { environment } from '@env/environment';
import { SocketClientState } from '../models/SocketClientState';

@Injectable({
  providedIn: 'root'
})
export class SocketClientService {
  private client!: StompJs.Client;
  private state!: BehaviorSubject<SocketClientState>;
  private subscriptions: string[];

  public constructor() {
    this.subscriptions = [];
    this._connection();
  }

  public stompFailureCallback() {
    this.connect().pipe(first()).subscribe(inst => inst.disconnect(() => null));
    setTimeout(() => {
      this._connection();
    }, 5000);
    console.log('Reconnecting in 5 seconds');
  }

  public ngOnDestroy(): void {
    this.connect().pipe(first()).subscribe(inst => inst.disconnect(() => null));
  }

  private _connection() {
    this.client = StompJs.over(new SockJS(environment.api));
    // this.client.debug = (str) => {}; 
    this.state = new BehaviorSubject<SocketClientState>(SocketClientState.ATTEMPTING);
    this.client.connect({}, () => {
      this.state.next(SocketClientState.CONNECTED);
    }, () => this.stompFailureCallback());
  }

  public connect(): Observable<StompJs.Client> {
    return new Observable<StompJs.Client>(observer => {
      this.state.pipe(filter(state => state === SocketClientState.CONNECTED)).subscribe(() => {
        observer.next(this.client);
      });
    });
  }

  public onMessage(prefix: string, handle = SocketClientService.jsonHandler): Observable<any> {
    return this.connect().pipe(first(), switchMap(client => {
      return new Observable<any>(observer => {
        return client.subscribe(prefix, message => observer.next(handle(message)))
      });
    }));
  }


  public onPlainMessage(prefix: string): Observable<string> {
    return this.onMessage(prefix, SocketClientService.textHandler);
  }

  public send(prefix: string, payload: any): void {
    this.connect()
      .pipe(first())
      .subscribe(inst => inst.send(prefix, {}, JSON.stringify(payload)));
  }

  static jsonHandler(message: StompJs.Message): any {
    return JSON.parse(message.body);
  }

  static textHandler(message: StompJs.Message): string {
    return message.body;
  }

}