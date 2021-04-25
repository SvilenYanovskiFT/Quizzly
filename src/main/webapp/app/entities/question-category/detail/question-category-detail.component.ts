import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionCategory } from '../question-category.model';

@Component({
  selector: 'jhi-question-category-detail',
  templateUrl: './question-category-detail.component.html',
})
export class QuestionCategoryDetailComponent implements OnInit {
  questionCategory: IQuestionCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionCategory }) => {
      this.questionCategory = questionCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
