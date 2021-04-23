import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestion, Question } from '../question.model';
import { QuestionService } from '../service/question.service';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';

@Component({
  selector: 'jhi-question-update',
  templateUrl: './question-update.component.html',
})
export class QuestionUpdateComponent implements OnInit {
  isSaving = false;

  quizzesSharedCollection: IQuiz[] = [];

  editForm = this.fb.group({
    id: [],
    sortOrder: [],
    text: [],
    image: [],
    answerA: [],
    answerB: [],
    answerC: [],
    answerD: [],
    correctAnswer: [],
    timeLimit: [],
    quiz: [],
  });

  constructor(
    protected questionService: QuestionService,
    protected quizService: QuizService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ question }) => {
      this.updateForm(question);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const question = this.createFromForm();
    if (question.id !== undefined) {
      this.subscribeToSaveResponse(this.questionService.update(question));
    } else {
      this.subscribeToSaveResponse(this.questionService.create(question));
    }
  }

  trackQuizById(index: number, item: IQuiz): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestion>>): void {
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

  protected updateForm(question: IQuestion): void {
    this.editForm.patchValue({
      id: question.id,
      sortOrder: question.sortOrder,
      text: question.text,
      image: question.image,
      answerA: question.answerA,
      answerB: question.answerB,
      answerC: question.answerC,
      answerD: question.answerD,
      correctAnswer: question.correctAnswer,
      timeLimit: question.timeLimit,
      quiz: question.quiz,
    });

    this.quizzesSharedCollection = this.quizService.addQuizToCollectionIfMissing(this.quizzesSharedCollection, question.quiz);
  }

  protected loadRelationshipsOptions(): void {
    this.quizService
      .query()
      .pipe(map((res: HttpResponse<IQuiz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuiz[]) => this.quizService.addQuizToCollectionIfMissing(quizzes, this.editForm.get('quiz')!.value)))
      .subscribe((quizzes: IQuiz[]) => (this.quizzesSharedCollection = quizzes));
  }

  protected createFromForm(): IQuestion {
    return {
      ...new Question(),
      id: this.editForm.get(['id'])!.value,
      sortOrder: this.editForm.get(['sortOrder'])!.value,
      text: this.editForm.get(['text'])!.value,
      image: this.editForm.get(['image'])!.value,
      answerA: this.editForm.get(['answerA'])!.value,
      answerB: this.editForm.get(['answerB'])!.value,
      answerC: this.editForm.get(['answerC'])!.value,
      answerD: this.editForm.get(['answerD'])!.value,
      correctAnswer: this.editForm.get(['correctAnswer'])!.value,
      timeLimit: this.editForm.get(['timeLimit'])!.value,
      quiz: this.editForm.get(['quiz'])!.value,
    };
  }
}
