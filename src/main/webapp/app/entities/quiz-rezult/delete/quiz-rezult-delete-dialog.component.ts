import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuizRezult } from '../quiz-rezult.model';
import { QuizRezultService } from '../service/quiz-rezult.service';

@Component({
  templateUrl: './quiz-rezult-delete-dialog.component.html',
})
export class QuizRezultDeleteDialogComponent {
  quizRezult?: IQuizRezult;

  constructor(protected quizRezultService: QuizRezultService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quizRezultService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
