import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizResult, QuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';

@Injectable({ providedIn: 'root' })
export class QuizResultRoutingResolveService implements Resolve<IQuizResult> {
  constructor(protected service: QuizResultService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuizResult> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((quizResult: HttpResponse<QuizResult>) => {
          if (quizResult.body) {
            return of(quizResult.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuizResult());
  }
}
