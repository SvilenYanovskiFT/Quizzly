import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { QuizRezultComponent } from './list/quiz-rezult.component';
import { QuizRezultDetailComponent } from './detail/quiz-rezult-detail.component';
import { QuizRezultUpdateComponent } from './update/quiz-rezult-update.component';
import { QuizRezultDeleteDialogComponent } from './delete/quiz-rezult-delete-dialog.component';
import { QuizRezultRoutingModule } from './route/quiz-rezult-routing.module';

@NgModule({
  imports: [SharedModule, QuizRezultRoutingModule],
  declarations: [QuizRezultComponent, QuizRezultDetailComponent, QuizRezultUpdateComponent, QuizRezultDeleteDialogComponent],
  entryComponents: [QuizRezultDeleteDialogComponent],
})
export class QuizRezultModule {}
