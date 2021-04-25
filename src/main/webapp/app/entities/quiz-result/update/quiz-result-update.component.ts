import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuizResult, QuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

@Component({
  selector: 'jhi-quiz-result-update',
  templateUrl: './quiz-result-update.component.html',
})
export class QuizResultUpdateComponent implements OnInit {
  isSaving = false;

  quizzesSharedCollection: IQuiz[] = [];
  userAccountsSharedCollection: IUserAccount[] = [];

  editForm = this.fb.group({
    id: [],
    score: [],
    rank: [],
    quiz: [],
    quizResult: [],
  });

  constructor(
    protected quizResultService: QuizResultService,
    protected quizService: QuizService,
    protected userAccountService: UserAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizResult }) => {
      this.updateForm(quizResult);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizResult = this.createFromForm();
    if (quizResult.id !== undefined) {
      this.subscribeToSaveResponse(this.quizResultService.update(quizResult));
    } else {
      this.subscribeToSaveResponse(this.quizResultService.create(quizResult));
    }
  }

  trackQuizById(index: number, item: IQuiz): number {
    return item.id!;
  }

  trackUserAccountById(index: number, item: IUserAccount): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizResult>>): void {
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

  protected updateForm(quizResult: IQuizResult): void {
    this.editForm.patchValue({
      id: quizResult.id,
      score: quizResult.score,
      rank: quizResult.rank,
      quiz: quizResult.quiz,
      quizResult: quizResult.quizResult,
    });

    this.quizzesSharedCollection = this.quizService.addQuizToCollectionIfMissing(this.quizzesSharedCollection, quizResult.quiz);
    this.userAccountsSharedCollection = this.userAccountService.addUserAccountToCollectionIfMissing(
      this.userAccountsSharedCollection,
      quizResult.quizResult
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quizService
      .query()
      .pipe(map((res: HttpResponse<IQuiz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuiz[]) => this.quizService.addQuizToCollectionIfMissing(quizzes, this.editForm.get('quiz')!.value)))
      .subscribe((quizzes: IQuiz[]) => (this.quizzesSharedCollection = quizzes));

    this.userAccountService
      .query()
      .pipe(map((res: HttpResponse<IUserAccount[]>) => res.body ?? []))
      .pipe(
        map((userAccounts: IUserAccount[]) =>
          this.userAccountService.addUserAccountToCollectionIfMissing(userAccounts, this.editForm.get('quizResult')!.value)
        )
      )
      .subscribe((userAccounts: IUserAccount[]) => (this.userAccountsSharedCollection = userAccounts));
  }

  protected createFromForm(): IQuizResult {
    return {
      ...new QuizResult(),
      id: this.editForm.get(['id'])!.value,
      score: this.editForm.get(['score'])!.value,
      rank: this.editForm.get(['rank'])!.value,
      quiz: this.editForm.get(['quiz'])!.value,
      quizResult: this.editForm.get(['quizResult'])!.value,
    };
  }
}
