import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home/home.component';
import { MyQuestionsComponent } from './my-questions/my-questions.component';
import { MyQuizzesComponent } from './my-quizzes/my-quizzes.component';
import { MyDashboardComponent } from './my-dashboard/my-dashboard.component';
import { StartQuizComponent } from './start-quiz/start-quiz.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(HOME_ROUTE)],
  declarations: [HomeComponent, MyQuestionsComponent, MyQuizzesComponent, MyDashboardComponent, StartQuizComponent],
})
export class HomeModule {}
