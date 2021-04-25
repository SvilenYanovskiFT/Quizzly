import { Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MyDashboardComponent } from 'app/home/my-dashboard/my-dashboard.component';
import { MyQuizzesComponent } from 'app/home/my-quizzes/my-quizzes.component';
import { MyQuestionsComponent } from 'app/home/my-questions/my-questions.component';
import { StartQuizComponent } from 'app/home/start-quiz/start-quiz.component';

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      pageTitle: 'Quizzes for life!',
    },
  },
  {
    path: 'my-dashboard',
    component: MyDashboardComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Quizzes Dashboard!',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'my-quizzes',
    component: MyQuizzesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Your Quizzes!',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'my-questions',
    component: MyQuestionsComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Your Questions!',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'start-quiz/:code',
    component: StartQuizComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Achtung! Quiz on horizon!',
    },
    canActivate: [UserRouteAccessService],
  },
];
