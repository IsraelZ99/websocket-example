import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, filter, first, Observable, switchMap } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { environment } from '@env/environment';
import { SocketClientState } from '../models/SocketClientState';
import * as StompJs from 'stompjs'
import { StompSubscription } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root'
})
export class SocketClientService implements OnDestroy {

  private client: StompJs.Client;
  private state: BehaviorSubject<SocketClientState>;

  public constructor() {
    this.client = StompJs.over(new SockJS(environment.api));
    this.state = new BehaviorSubject<SocketClientState>(SocketClientState.ATTEMPTING);
    this.client.connect({}, () => {
      // this.state.next(SocketClientState.CONNECTED);
    });
  }

  public ngOnDestroy(): void {
    this.connect().pipe(first()).subscribe(client => client.disconnect(() => null));
  }

  private connect(): Observable<StompJs.Client> {
    return new Observable<StompJs.Client>(observer => {
      this.state.pipe(filter(state => state === SocketClientState.CONNECTED)).subscribe(() => {
        observer.next(this.client);
      })
    });
  }

  /************************************** Retrieving messages  **************************************/

  public onMessage(path: string, handler = SocketClientService.jsonHandler): Observable<any> {
    return this.connect().pipe(first(), switchMap(client => {
      return new Observable<any>(observer => {
        const subscription: StompSubscription = client.subscribe(path, message => {
          observer.next(handler(message));
        });
        return () => client.unsubscribe(subscription.id);
      });
    }))
  }

  static jsonHandler(message: any): any {
    return JSON.parse(message.body);
  }

  static textHandler(message: any): string {
    return message.body;
  }

  public onPLainMessage(path: string): Observable<string> {
    return this.onMessage(path, SocketClientService.textHandler);
  }

  /************************************** Retrieving messages  **************************************/


  /************************************** Send messages  **************************************/
  public send(path: string, payload: any): void {
    this.connect().pipe(first())
      .subscribe(client => client.send(path, {}, JSON.stringify(payload)));
  }

}
