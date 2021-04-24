import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuizResultComponent } from '../list/quiz-result.component';
import { QuizResultDetailComponent } from '../detail/quiz-result-detail.component';
import { QuizResultUpdateComponent } from '../update/quiz-result-update.component';
import { QuizResultRoutingResolveService } from './quiz-result-routing-resolve.service';

const quizResultRoute: Routes = [
  {
    path: '',
    component: QuizResultComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizResultDetailComponent,
    resolve: {
      quizResult: QuizResultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizResultUpdateComponent,
    resolve: {
      quizResult: QuizResultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizResultUpdateComponent,
    resolve: {
      quizResult: QuizResultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(quizResultRoute)],
  exports: [RouterModule],
})
export class QuizResultRoutingModule {}
