jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuizResultService } from '../service/quiz-result.service';
import { IQuizResult, QuizResult } from '../quiz-result.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

import { QuizResultUpdateComponent } from './quiz-result-update.component';

describe('Component Tests', () => {
  describe('QuizResult Management Update Component', () => {
    let comp: QuizResultUpdateComponent;
    let fixture: ComponentFixture<QuizResultUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let quizResultService: QuizResultService;
    let quizService: QuizService;
    let userAccountService: UserAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuizResultUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuizResultUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuizResultUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      quizResultService = TestBed.inject(QuizResultService);
      quizService = TestBed.inject(QuizService);
      userAccountService = TestBed.inject(UserAccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Quiz query and add missing value', () => {
        const quizResult: IQuizResult = { id: 456 };
        const quiz: IQuiz = { id: 48355 };
        quizResult.quiz = quiz;

        const quizCollection: IQuiz[] = [{ id: 30503 }];
        spyOn(quizService, 'query').and.returnValue(of(new HttpResponse({ body: quizCollection })));
        const additionalQuizzes = [quiz];
        const expectedCollection: IQuiz[] = [...additionalQuizzes, ...quizCollection];
        spyOn(quizService, 'addQuizToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ quizResult });
        comp.ngOnInit();

        expect(quizService.query).toHaveBeenCalled();
        expect(quizService.addQuizToCollectionIfMissing).toHaveBeenCalledWith(quizCollection, ...additionalQuizzes);
        expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call UserAccount query and add missing value', () => {
        const quizResult: IQuizResult = { id: 456 };
        const quizResult: IUserAccount = { id: 31164 };
        quizResult.quizResult = quizResult;

        const userAccountCollection: IUserAccount[] = [{ id: 69727 }];
        spyOn(userAccountService, 'query').and.returnValue(of(new HttpResponse({ body: userAccountCollection })));
        const additionalUserAccounts = [quizResult];
        const expectedCollection: IUserAccount[] = [...additionalUserAccounts, ...userAccountCollection];
        spyOn(userAccountService, 'addUserAccountToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ quizResult });
        comp.ngOnInit();

        expect(userAccountService.query).toHaveBeenCalled();
        expect(userAccountService.addUserAccountToCollectionIfMissing).toHaveBeenCalledWith(
          userAccountCollection,
          ...additionalUserAccounts
        );
        expect(comp.userAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const quizResult: IQuizResult = { id: 456 };
        const quiz: IQuiz = { id: 28514 };
        quizResult.quiz = quiz;
        const quizResult: IUserAccount = { id: 29959 };
        quizResult.quizResult = quizResult;

        activatedRoute.data = of({ quizResult });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(quizResult));
        expect(comp.quizzesSharedCollection).toContain(quiz);
        expect(comp.userAccountsSharedCollection).toContain(quizResult);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quizResult = { id: 123 };
        spyOn(quizResultService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quizResult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quizResult }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(quizResultService.update).toHaveBeenCalledWith(quizResult);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quizResult = new QuizResult();
        spyOn(quizResultService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quizResult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quizResult }));
        saveSubject.complete();

        // THEN
        expect(quizResultService.create).toHaveBeenCalledWith(quizResult);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quizResult = { id: 123 };
        spyOn(quizResultService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quizResult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(quizResultService.update).toHaveBeenCalledWith(quizResult);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackQuizById', () => {
        it('Should return tracked Quiz primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuizById(0, entity);
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
    });
  });
});
