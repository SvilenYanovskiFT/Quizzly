jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionService } from '../service/question.service';
import { IQuestion, Question } from '../question.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { QuizService } from 'app/entities/quiz/service/quiz.service';

import { QuestionUpdateComponent } from './question-update.component';

describe('Component Tests', () => {
  describe('Question Management Update Component', () => {
    let comp: QuestionUpdateComponent;
    let fixture: ComponentFixture<QuestionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionService: QuestionService;
    let quizService: QuizService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuestionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      questionService = TestBed.inject(QuestionService);
      quizService = TestBed.inject(QuizService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Quiz query and add missing value', () => {
        const question: IQuestion = { id: 456 };
        const quiz: IQuiz = { id: 85618 };
        question.quiz = quiz;

        const quizCollection: IQuiz[] = [{ id: 609 }];
        spyOn(quizService, 'query').and.returnValue(of(new HttpResponse({ body: quizCollection })));
        const additionalQuizzes = [quiz];
        const expectedCollection: IQuiz[] = [...additionalQuizzes, ...quizCollection];
        spyOn(quizService, 'addQuizToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ question });
        comp.ngOnInit();

        expect(quizService.query).toHaveBeenCalled();
        expect(quizService.addQuizToCollectionIfMissing).toHaveBeenCalledWith(quizCollection, ...additionalQuizzes);
        expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const question: IQuestion = { id: 456 };
        const quiz: IQuiz = { id: 84883 };
        question.quiz = quiz;

        activatedRoute.data = of({ question });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(question));
        expect(comp.quizzesSharedCollection).toContain(quiz);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const question = { id: 123 };
        spyOn(questionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ question });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: question }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(questionService.update).toHaveBeenCalledWith(question);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const question = new Question();
        spyOn(questionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ question });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: question }));
        saveSubject.complete();

        // THEN
        expect(questionService.create).toHaveBeenCalledWith(question);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const question = { id: 123 };
        spyOn(questionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ question });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(questionService.update).toHaveBeenCalledWith(question);
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
    });
  });
});
