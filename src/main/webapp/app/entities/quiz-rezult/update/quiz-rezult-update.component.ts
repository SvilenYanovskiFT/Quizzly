import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuizRezult, QuizRezult } from '../quiz-rezult.model';
import { QuizRezultService } from '../service/quiz-rezult.service';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

@Component({
  selector: 'jhi-quiz-rezult-update',
  templateUrl: './quiz-rezult-update.component.html',
})
export class QuizRezultUpdateComponent implements OnInit {
  isSaving = false;

  quizzesSharedCollection: IQuiz[] = [];
  userAccountsSharedCollection: IUserAccount[] = [];

  editForm = this.fb.group({
    id: [],
    score: [],
    rank: [],
    quiz: [],
    quizRezult: [],
  });

  constructor(
    protected quizRezultService: QuizRezultService,
    protected quizService: QuizService,
    protected userAccountService: UserAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizRezult }) => {
      this.updateForm(quizRezult);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizRezult = this.createFromForm();
    if (quizRezult.id !== undefined) {
      this.subscribeToSaveResponse(this.quizRezultService.update(quizRezult));
    } else {
      this.subscribeToSaveResponse(this.quizRezultService.create(quizRezult));
    }
  }

  trackQuizById(index: number, item: IQuiz): number {
    return item.id!;
  }

  trackUserAccountById(index: number, item: IUserAccount): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizRezult>>): void {
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

  protected updateForm(quizRezult: IQuizRezult): void {
    this.editForm.patchValue({
      id: quizRezult.id,
      score: quizRezult.score,
      rank: quizRezult.rank,
      quiz: quizRezult.quiz,
      quizRezult: quizRezult.quizRezult,
    });

    this.quizzesSharedCollection = this.quizService.addQuizToCollectionIfMissing(this.quizzesSharedCollection, quizRezult.quiz);
    this.userAccountsSharedCollection = this.userAccountService.addUserAccountToCollectionIfMissing(
      this.userAccountsSharedCollection,
      quizRezult.quizRezult
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
          this.userAccountService.addUserAccountToCollectionIfMissing(userAccounts, this.editForm.get('quizRezult')!.value)
        )
      )
      .subscribe((userAccounts: IUserAccount[]) => (this.userAccountsSharedCollection = userAccounts));
  }

  protected createFromForm(): IQuizRezult {
    return {
      ...new QuizRezult(),
      id: this.editForm.get(['id'])!.value,
      score: this.editForm.get(['score'])!.value,
      rank: this.editForm.get(['rank'])!.value,
      quiz: this.editForm.get(['quiz'])!.value,
      quizRezult: this.editForm.get(['quizRezult'])!.value,
    };
  }
}
