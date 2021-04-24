import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';
import { QuizResultDeleteDialogComponent } from '../delete/quiz-result-delete-dialog.component';

@Component({
  selector: 'jhi-quiz-result',
  templateUrl: './quiz-result.component.html',
})
export class QuizResultComponent implements OnInit {
  quizResults?: IQuizResult[];
  isLoading = false;

  constructor(protected quizResultService: QuizResultService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.quizResultService.query().subscribe(
      (res: HttpResponse<IQuizResult[]>) => {
        this.isLoading = false;
        this.quizResults = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IQuizResult): number {
    return item.id!;
  }

  delete(quizResult: IQuizResult): void {
    const modalRef = this.modalService.open(QuizResultDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.quizResult = quizResult;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
