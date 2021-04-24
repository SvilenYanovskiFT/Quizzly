import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuizRezult } from '../quiz-rezult.model';

@Component({
  selector: 'jhi-quiz-rezult-detail',
  templateUrl: './quiz-rezult-detail.component.html',
})
export class QuizRezultDetailComponent implements OnInit {
  quizRezult: IQuizRezult | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizRezult }) => {
      this.quizRezult = quizRezult;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
