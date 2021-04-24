import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuizRezult } from '../quiz-rezult.model';
import { QuizRezultService } from '../service/quiz-rezult.service';
import { QuizRezultDeleteDialogComponent } from '../delete/quiz-rezult-delete-dialog.component';

@Component({
  selector: 'jhi-quiz-rezult',
  templateUrl: './quiz-rezult.component.html',
})
export class QuizRezultComponent implements OnInit {
  quizRezults?: IQuizRezult[];
  isLoading = false;

  constructor(protected quizRezultService: QuizRezultService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.quizRezultService.query().subscribe(
      (res: HttpResponse<IQuizRezult[]>) => {
        this.isLoading = false;
        this.quizRezults = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IQuizRezult): number {
    return item.id!;
  }

  delete(quizRezult: IQuizRezult): void {
    const modalRef = this.modalService.open(QuizRezultDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.quizRezult = quizRezult;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
