import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionCategory, QuestionCategory } from '../question-category.model';
import { QuestionCategoryService } from '../service/question-category.service';

@Injectable({ providedIn: 'root' })
export class QuestionCategoryRoutingResolveService implements Resolve<IQuestionCategory> {
  constructor(protected service: QuestionCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionCategory: HttpResponse<QuestionCategory>) => {
          if (questionCategory.body) {
            return of(questionCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuestionCategory());
  }
}
