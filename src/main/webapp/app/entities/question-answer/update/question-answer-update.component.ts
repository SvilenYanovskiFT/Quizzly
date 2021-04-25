import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';
import { QuestionAnswerService } from '../service/question-answer.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';
import { IQuizResult } from 'app/entities/quiz-result/quiz-result.model';
import { QuizResultService } from 'app/entities/quiz-result/service/quiz-result.service';

@Component({
  selector: 'jhi-question-answer-update',
  templateUrl: './question-answer-update.component.html',
})
export class QuestionAnswerUpdateComponent implements OnInit {
  isSaving = false;

  questionsSharedCollection: IQuestion[] = [];
  userAccountsSharedCollection: IUserAccount[] = [];
  quizResultsSharedCollection: IQuizResult[] = [];

  editForm = this.fb.group({
    id: [],
    timeTaken: [],
    success: [],
    answer: [],
    question: [],
    participant: [],
    rezult: [],
  });

  constructor(
    protected questionAnswerService: QuestionAnswerService,
    protected questionService: QuestionService,
    protected userAccountService: UserAccountService,
    protected quizResultService: QuizResultService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionAnswer }) => {
      this.updateForm(questionAnswer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionAnswer = this.createFromForm();
    if (questionAnswer.id !== undefined) {
      this.subscribeToSaveResponse(this.questionAnswerService.update(questionAnswer));
    } else {
      this.subscribeToSaveResponse(this.questionAnswerService.create(questionAnswer));
    }
  }

  trackQuestionById(index: number, item: IQuestion): number {
    return item.id!;
  }

  trackUserAccountById(index: number, item: IUserAccount): number {
    return item.id!;
  }

  trackQuizResultById(index: number, item: IQuizResult): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionAnswer>>): void {
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

  protected updateForm(questionAnswer: IQuestionAnswer): void {
    this.editForm.patchValue({
      id: questionAnswer.id,
      timeTaken: questionAnswer.timeTaken,
      success: questionAnswer.success,
      answer: questionAnswer.answer,
      question: questionAnswer.question,
      participant: questionAnswer.participant,
      rezult: questionAnswer.rezult,
    });

    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing(
      this.questionsSharedCollection,
      questionAnswer.question
    );
    this.userAccountsSharedCollection = this.userAccountService.addUserAccountToCollectionIfMissing(
      this.userAccountsSharedCollection,
      questionAnswer.participant
    );
    this.quizResultsSharedCollection = this.quizResultService.addQuizResultToCollectionIfMissing(
      this.quizResultsSharedCollection,
      questionAnswer.rezult
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing(questions, this.editForm.get('question')!.value)
        )
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));

    this.userAccountService
      .query()
      .pipe(map((res: HttpResponse<IUserAccount[]>) => res.body ?? []))
      .pipe(
        map((userAccounts: IUserAccount[]) =>
          this.userAccountService.addUserAccountToCollectionIfMissing(userAccounts, this.editForm.get('participant')!.value)
        )
      )
      .subscribe((userAccounts: IUserAccount[]) => (this.userAccountsSharedCollection = userAccounts));

    this.quizResultService
      .query()
      .pipe(map((res: HttpResponse<IQuizResult[]>) => res.body ?? []))
      .pipe(
        map((quizResults: IQuizResult[]) =>
          this.quizResultService.addQuizResultToCollectionIfMissing(quizResults, this.editForm.get('rezult')!.value)
        )
      )
      .subscribe((quizResults: IQuizResult[]) => (this.quizResultsSharedCollection = quizResults));
  }

  protected createFromForm(): IQuestionAnswer {
    return {
      ...new QuestionAnswer(),
      id: this.editForm.get(['id'])!.value,
      timeTaken: this.editForm.get(['timeTaken'])!.value,
      success: this.editForm.get(['success'])!.value,
      answer: this.editForm.get(['answer'])!.value,
      question: this.editForm.get(['question'])!.value,
      participant: this.editForm.get(['participant'])!.value,
      rezult: this.editForm.get(['rezult'])!.value,
    };
  }
}
