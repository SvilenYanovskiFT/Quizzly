jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionAnswerService } from '../service/question-answer.service';
import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';
import { IQuizRezult } from 'app/entities/quiz-rezult/quiz-rezult.model';
import { QuizRezultService } from 'app/entities/quiz-rezult/service/quiz-rezult.service';

import { QuestionAnswerUpdateComponent } from './question-answer-update.component';

describe('Component Tests', () => {
  describe('QuestionAnswer Management Update Component', () => {
    let comp: QuestionAnswerUpdateComponent;
    let fixture: ComponentFixture<QuestionAnswerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionAnswerService: QuestionAnswerService;
    let questionService: QuestionService;
    let userAccountService: UserAccountService;
    let quizRezultService: QuizRezultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionAnswerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuestionAnswerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionAnswerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      questionAnswerService = TestBed.inject(QuestionAnswerService);
      questionService = TestBed.inject(QuestionService);
      userAccountService = TestBed.inject(UserAccountService);
      quizRezultService = TestBed.inject(QuizRezultService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Question query and add missing value', () => {
        const questionAnswer: IQuestionAnswer = { id: 456 };
        const question: IQuestion = { id: 94612 };
        questionAnswer.question = question;

        const questionCollection: IQuestion[] = [{ id: 76624 }];
        spyOn(questionService, 'query').and.returnValue(of(new HttpResponse({ body: questionCollection })));
        const additionalQuestions = [question];
        const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
        spyOn(questionService, 'addQuestionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        expect(questionService.query).toHaveBeenCalled();
        expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(questionCollection, ...additionalQuestions);
        expect(comp.questionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call UserAccount query and add missing value', () => {
        const questionAnswer: IQuestionAnswer = { id: 456 };
        const participant: IUserAccount = { id: 91511 };
        questionAnswer.participant = participant;

        const userAccountCollection: IUserAccount[] = [{ id: 35576 }];
        spyOn(userAccountService, 'query').and.returnValue(of(new HttpResponse({ body: userAccountCollection })));
        const additionalUserAccounts = [participant];
        const expectedCollection: IUserAccount[] = [...additionalUserAccounts, ...userAccountCollection];
        spyOn(userAccountService, 'addUserAccountToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        expect(userAccountService.query).toHaveBeenCalled();
        expect(userAccountService.addUserAccountToCollectionIfMissing).toHaveBeenCalledWith(
          userAccountCollection,
          ...additionalUserAccounts
        );
        expect(comp.userAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call QuizRezult query and add missing value', () => {
        const questionAnswer: IQuestionAnswer = { id: 456 };
        const rezult: IQuizRezult = { id: 65268 };
        questionAnswer.rezult = rezult;

        const quizRezultCollection: IQuizRezult[] = [{ id: 54226 }];
        spyOn(quizRezultService, 'query').and.returnValue(of(new HttpResponse({ body: quizRezultCollection })));
        const additionalQuizRezults = [rezult];
        const expectedCollection: IQuizRezult[] = [...additionalQuizRezults, ...quizRezultCollection];
        spyOn(quizRezultService, 'addQuizRezultToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        expect(quizRezultService.query).toHaveBeenCalled();
        expect(quizRezultService.addQuizRezultToCollectionIfMissing).toHaveBeenCalledWith(quizRezultCollection, ...additionalQuizRezults);
        expect(comp.quizRezultsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const questionAnswer: IQuestionAnswer = { id: 456 };
        const question: IQuestion = { id: 66635 };
        questionAnswer.question = question;
        const participant: IUserAccount = { id: 94355 };
        questionAnswer.participant = participant;
        const rezult: IQuizRezult = { id: 6087 };
        questionAnswer.rezult = rezult;

        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(questionAnswer));
        expect(comp.questionsSharedCollection).toContain(question);
        expect(comp.userAccountsSharedCollection).toContain(participant);
        expect(comp.quizRezultsSharedCollection).toContain(rezult);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const questionAnswer = { id: 123 };
        spyOn(questionAnswerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionAnswer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(questionAnswerService.update).toHaveBeenCalledWith(questionAnswer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const questionAnswer = new QuestionAnswer();
        spyOn(questionAnswerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionAnswer }));
        saveSubject.complete();

        // THEN
        expect(questionAnswerService.create).toHaveBeenCalledWith(questionAnswer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const questionAnswer = { id: 123 };
        spyOn(questionAnswerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionAnswer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(questionAnswerService.update).toHaveBeenCalledWith(questionAnswer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackQuestionById', () => {
        it('Should return tracked Question primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuestionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserAccountById', () => {
        it('Should return tracked UserAccount primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserAccountById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackQuizRezultById', () => {
        it('Should return tracked QuizRezult primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuizRezultById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
