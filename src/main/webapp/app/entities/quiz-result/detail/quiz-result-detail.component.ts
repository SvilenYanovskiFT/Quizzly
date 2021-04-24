import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuizResult } from '../quiz-result.model';

@Component({
  selector: 'jhi-quiz-result-detail',
  templateUrl: './quiz-result-detail.component.html',
})
export class QuizResultDetailComponent implements OnInit {
  quizResult: IQuizResult | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizResult }) => {
      this.quizResult = quizResult;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
