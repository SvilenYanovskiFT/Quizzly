import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';

@Component({
  templateUrl: './quiz-result-delete-dialog.component.html',
})
export class QuizResultDeleteDialogComponent {
  quizResult?: IQuizResult;

  constructor(protected quizResultService: QuizResultService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quizResultService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
