import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { QuestionCategoryComponent } from './list/question-category.component';
import { QuestionCategoryDetailComponent } from './detail/question-category-detail.component';
import { QuestionCategoryUpdateComponent } from './update/question-category-update.component';
import { QuestionCategoryDeleteDialogComponent } from './delete/question-category-delete-dialog.component';
import { QuestionCategoryRoutingModule } from './route/question-category-routing.module';

@NgModule({
  imports: [SharedModule, QuestionCategoryRoutingModule],
  declarations: [
    QuestionCategoryComponent,
    QuestionCategoryDetailComponent,
    QuestionCategoryUpdateComponent,
    QuestionCategoryDeleteDialogComponent,
  ],
  entryComponents: [QuestionCategoryDeleteDialogComponent],
})
export class QuestionCategoryModule {}
