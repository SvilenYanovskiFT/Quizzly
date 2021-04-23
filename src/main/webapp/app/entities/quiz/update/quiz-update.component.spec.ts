jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuizService } from '../service/quiz.service';
import { IQuiz, Quiz } from '../quiz.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

import { QuizUpdateComponent } from './quiz-update.component';

describe('Component Tests', () => {
  describe('Quiz Management Update Component', () => {
    let comp: QuizUpdateComponent;
    let fixture: ComponentFixture<QuizUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let quizService: QuizService;
    let userAccountService: UserAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuizUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuizUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuizUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      quizService = TestBed.inject(QuizService);
      userAccountService = TestBed.inject(UserAccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call UserAccount query and add missing value', () => {
        const quiz: IQuiz = { id: 456 };
        const owner: IUserAccount = { id: 52438 };
        quiz.owner = owner;

        const userAccountCollection: IUserAccount[] = [{ id: 49531 }];
        spyOn(userAccountService, 'query').and.returnValue(of(new HttpResponse({ body: userAccountCollection })));
        const additionalUserAccounts = [owner];
        const expectedCollection: IUserAccount[] = [...additionalUserAccounts, ...userAccountCollection];
        spyOn(userAccountService, 'addUserAccountToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ quiz });
        comp.ngOnInit();

        expect(userAccountService.query).toHaveBeenCalled();
        expect(userAccountService.addUserAccountToCollectionIfMissing).toHaveBeenCalledWith(
          userAccountCollection,
          ...additionalUserAccounts
        );
        expect(comp.userAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const quiz: IQuiz = { id: 456 };
        const owner: IUserAccount = { id: 13969 };
        quiz.owner = owner;

        activatedRoute.data = of({ quiz });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(quiz));
        expect(comp.userAccountsSharedCollection).toContain(owner);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quiz = { id: 123 };
        spyOn(quizService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quiz });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quiz }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(quizService.update).toHaveBeenCalledWith(quiz);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quiz = new Quiz();
        spyOn(quizService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quiz });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quiz }));
        saveSubject.complete();

        // THEN
        expect(quizService.create).toHaveBeenCalledWith(quiz);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quiz = { id: 123 };
        spyOn(quizService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quiz });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(quizService.update).toHaveBeenCalledWith(quiz);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
