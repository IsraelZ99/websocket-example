import { Component, OnDestroy, OnInit } from '@angular/core';
import { ErrorService } from 'app/services/error.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-error-handler',
  templateUrl: './error-handler.component.html',
  styleUrls: ['./error-handler.component.scss']
})
export class ErrorHandlerComponent implements OnInit, OnDestroy {

  public message: string;
  private unsubscribeSubject: Subject<void>;

  public constructor(private errorService: ErrorService) {
    this.message = "";
    this.unsubscribeSubject = new Subject();
  }

  public ngOnInit(): void {
    this.errorService.onError()
      .pipe(takeUntil(this.unsubscribeSubject))
      .subscribe(message => this.message = message);
  }

  public ngOnDestroy(): void {
    this.unsubscribeSubject.next();
    this.unsubscribeSubject.complete();
  }

  public clear(event: Event) {
    this.message = "";
    event.preventDefault();
  }

}
