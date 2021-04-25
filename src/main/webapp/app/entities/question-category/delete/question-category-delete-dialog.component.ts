import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionCategory } from '../question-category.model';
import { QuestionCategoryService } from '../service/question-category.service';

@Component({
  templateUrl: './question-category-delete-dialog.component.html',
})
export class QuestionCategoryDeleteDialogComponent {
  questionCategory?: IQuestionCategory;

  constructor(protected questionCategoryService: QuestionCategoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
