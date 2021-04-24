import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { QuizResultComponent } from './list/quiz-result.component';
import { QuizResultDetailComponent } from './detail/quiz-result-detail.component';
import { QuizResultUpdateComponent } from './update/quiz-result-update.component';
import { QuizResultDeleteDialogComponent } from './delete/quiz-result-delete-dialog.component';
import { QuizResultRoutingModule } from './route/quiz-result-routing.module';

@NgModule({
  imports: [SharedModule, QuizResultRoutingModule],
  declarations: [QuizResultComponent, QuizResultDetailComponent, QuizResultUpdateComponent, QuizResultDeleteDialogComponent],
  entryComponents: [QuizResultDeleteDialogComponent],
})
export class QuizResultModule {}
