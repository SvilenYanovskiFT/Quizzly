import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuizRezultComponent } from '../list/quiz-rezult.component';
import { QuizRezultDetailComponent } from '../detail/quiz-rezult-detail.component';
import { QuizRezultUpdateComponent } from '../update/quiz-rezult-update.component';
import { QuizRezultRoutingResolveService } from './quiz-rezult-routing-resolve.service';

const quizRezultRoute: Routes = [
  {
    path: '',
    component: QuizRezultComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizRezultDetailComponent,
    resolve: {
      quizRezult: QuizRezultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizRezultUpdateComponent,
    resolve: {
      quizRezult: QuizRezultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizRezultUpdateComponent,
    resolve: {
      quizRezult: QuizRezultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(quizRezultRoute)],
  exports: [RouterModule],
})
export class QuizRezultRoutingModule {}
