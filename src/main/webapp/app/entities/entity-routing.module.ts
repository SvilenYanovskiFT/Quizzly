import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'question',
        data: { pageTitle: 'Questions' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      {
        path: 'quiz',
        data: { pageTitle: 'Quizzes' },
        loadChildren: () => import('./quiz/quiz.module').then(m => m.QuizModule),
      },
      {
        path: 'user-account',
        data: { pageTitle: 'UserAccounts' },
        loadChildren: () => import('./user-account/user-account.module').then(m => m.UserAccountModule),
      },
      {
        path: 'question-answer',
        data: { pageTitle: 'QuestionAnswers' },
        loadChildren: () => import('./question-answer/question-answer.module').then(m => m.QuestionAnswerModule),
      },
      {
        path: 'quiz-rezult',
        data: { pageTitle: 'QuizRezults' },
        loadChildren: () => import('./quiz-rezult/quiz-rezult.module').then(m => m.QuizRezultModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
