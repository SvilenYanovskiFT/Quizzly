import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IQuestionCategory, QuestionCategory } from '../question-category.model';
import { QuestionCategoryService } from '../service/question-category.service';

@Component({
  selector: 'jhi-question-category-update',
  templateUrl: './question-category-update.component.html',
})
export class QuestionCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected questionCategoryService: QuestionCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionCategory }) => {
      this.updateForm(questionCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionCategory = this.createFromForm();
    if (questionCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.questionCategoryService.update(questionCategory));
    } else {
      this.subscribeToSaveResponse(this.questionCategoryService.create(questionCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionCategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(questionCategory: IQuestionCategory): void {
    this.editForm.patchValue({
      id: questionCategory.id,
      name: questionCategory.name,
    });
  }

  protected createFromForm(): IQuestionCategory {
    return {
      ...new QuestionCategory(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
