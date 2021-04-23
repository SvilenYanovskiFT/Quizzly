import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuiz, Quiz } from '../quiz.model';
import { QuizService } from '../service/quiz.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

@Component({
  selector: 'jhi-quiz-update',
  templateUrl: './quiz-update.component.html',
})
export class QuizUpdateComponent implements OnInit {
  isSaving = false;

  userAccountsSharedCollection: IUserAccount[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    code: [],
    quizType: [],
    owner: [],
  });

  constructor(
    protected quizService: QuizService,
    protected userAccountService: UserAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quiz }) => {
      this.updateForm(quiz);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quiz = this.createFromForm();
    if (quiz.id !== undefined) {
      this.subscribeToSaveResponse(this.quizService.update(quiz));
    } else {
      this.subscribeToSaveResponse(this.quizService.create(quiz));
    }
  }

  trackUserAccountById(index: number, item: IUserAccount): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuiz>>): void {
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

  protected updateForm(quiz: IQuiz): void {
    this.editForm.patchValue({
      id: quiz.id,
      name: quiz.name,
      code: quiz.code,
      quizType: quiz.quizType,
      owner: quiz.owner,
    });

    this.userAccountsSharedCollection = this.userAccountService.addUserAccountToCollectionIfMissing(
      this.userAccountsSharedCollection,
      quiz.owner
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userAccountService
      .query()
      .pipe(map((res: HttpResponse<IUserAccount[]>) => res.body ?? []))
      .pipe(
        map((userAccounts: IUserAccount[]) =>
          this.userAccountService.addUserAccountToCollectionIfMissing(userAccounts, this.editForm.get('owner')!.value)
        )
      )
      .subscribe((userAccounts: IUserAccount[]) => (this.userAccountsSharedCollection = userAccounts));
  }

  protected createFromForm(): IQuiz {
    return {
      ...new Quiz(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      code: this.editForm.get(['code'])!.value,
      quizType: this.editForm.get(['quizType'])!.value,
      owner: this.editForm.get(['owner'])!.value,
    };
  }
}
