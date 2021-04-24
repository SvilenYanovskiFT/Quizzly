import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizRezult, QuizRezult } from '../quiz-rezult.model';
import { QuizRezultService } from '../service/quiz-rezult.service';

@Injectable({ providedIn: 'root' })
export class QuizRezultRoutingResolveService implements Resolve<IQuizRezult> {
  constructor(protected service: QuizRezultService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuizRezult> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((quizRezult: HttpResponse<QuizRezult>) => {
          if (quizRezult.body) {
            return of(quizRezult.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuizRezult());
  }
}
