import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionCategoryComponent } from '../list/question-category.component';
import { QuestionCategoryDetailComponent } from '../detail/question-category-detail.component';
import { QuestionCategoryUpdateComponent } from '../update/question-category-update.component';
import { QuestionCategoryRoutingResolveService } from './question-category-routing-resolve.service';

const questionCategoryRoute: Routes = [
  {
    path: '',
    component: QuestionCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionCategoryDetailComponent,
    resolve: {
      questionCategory: QuestionCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionCategoryUpdateComponent,
    resolve: {
      questionCategory: QuestionCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionCategoryUpdateComponent,
    resolve: {
      questionCategory: QuestionCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionCategoryRoute)],
  exports: [RouterModule],
})
export class QuestionCategoryRoutingModule {}
