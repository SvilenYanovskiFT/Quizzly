jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuizRezultService } from '../service/quiz-rezult.service';
import { IQuizRezult, QuizRezult } from '../quiz-rezult.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

import { QuizRezultUpdateComponent } from './quiz-rezult-update.component';

describe('Component Tests', () => {
  describe('QuizRezult Management Update Component', () => {
    let comp: QuizRezultUpdateComponent;
    let fixture: ComponentFixture<QuizRezultUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let quizRezultService: QuizRezultService;
    let quizService: QuizService;
    let userAccountService: UserAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuizRezultUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuizRezultUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuizRezultUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      quizRezultService = TestBed.inject(QuizRezultService);
      quizService = TestBed.inject(QuizService);
      userAccountService = TestBed.inject(UserAccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Quiz query and add missing value', () => {
        const quizRezult: IQuizRezult = { id: 456 };
        const quiz: IQuiz = { id: 48355 };
        quizRezult.quiz = quiz;

        const quizCollection: IQuiz[] = [{ id: 30503 }];
        spyOn(quizService, 'query').and.returnValue(of(new HttpResponse({ body: quizCollection })));
        const additionalQuizzes = [quiz];
        const expectedCollection: IQuiz[] = [...additionalQuizzes, ...quizCollection];
        spyOn(quizService, 'addQuizToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ quizRezult });
        comp.ngOnInit();

        expect(quizService.query).toHaveBeenCalled();
        expect(quizService.addQuizToCollectionIfMissing).toHaveBeenCalledWith(quizCollection, ...additionalQuizzes);
        expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quizRezult = { id: 123 };
        spyOn(quizRezultService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quizRezult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quizRezult }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(quizRezultService.update).toHaveBeenCalledWith(quizRezult);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quizRezult = new QuizRezult();
        spyOn(quizRezultService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quizRezult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quizRezult }));
        saveSubject.complete();

        // THEN
        expect(quizRezultService.create).toHaveBeenCalledWith(quizRezult);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quizRezult = { id: 123 };
        spyOn(quizRezultService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quizRezult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(quizRezultService.update).toHaveBeenCalledWith(quizRezult);
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
