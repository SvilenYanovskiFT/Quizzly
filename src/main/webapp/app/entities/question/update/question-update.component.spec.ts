jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionService } from '../service/question.service';
import { IQuestion, Question } from '../question.model';
import { IQuestionCategory } from 'app/entities/question-category/question-category.model';
import { QuestionCategoryService } from 'app/entities/question-category/service/question-category.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

import { QuestionUpdateComponent } from './question-update.component';

describe('Component Tests', () => {
  describe('Question Management Update Component', () => {
    let comp: QuestionUpdateComponent;
    let fixture: ComponentFixture<QuestionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionService: QuestionService;
    let questionCategoryService: QuestionCategoryService;
    let userAccountService: UserAccountService;

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
      questionCategoryService = TestBed.inject(QuestionCategoryService);
      userAccountService = TestBed.inject(UserAccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call QuestionCategory query and add missing value', () => {
        const question: IQuestion = { id: 456 };
        const questionCategory: IQuestionCategory = { id: 76909 };
        question.questionCategory = questionCategory;

        const questionCategoryCollection: IQuestionCategory[] = [{ id: 53556 }];
        spyOn(questionCategoryService, 'query').and.returnValue(of(new HttpResponse({ body: questionCategoryCollection })));
        const additionalQuestionCategories = [questionCategory];
        const expectedCollection: IQuestionCategory[] = [...additionalQuestionCategories, ...questionCategoryCollection];
        spyOn(questionCategoryService, 'addQuestionCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ question });
        comp.ngOnInit();

        expect(questionCategoryService.query).toHaveBeenCalled();
        expect(questionCategoryService.addQuestionCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          questionCategoryCollection,
          ...additionalQuestionCategories
        );
        expect(comp.questionCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call UserAccount query and add missing value', () => {
        const question: IQuestion = { id: 456 };
        const createdBy: IUserAccount = { id: 52438 };
        question.createdBy = createdBy;

        const userAccountCollection: IUserAccount[] = [{ id: 49531 }];
        spyOn(userAccountService, 'query').and.returnValue(of(new HttpResponse({ body: userAccountCollection })));
        const additionalUserAccounts = [createdBy];
        const expectedCollection: IUserAccount[] = [...additionalUserAccounts, ...userAccountCollection];
        spyOn(userAccountService, 'addUserAccountToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ question });
        comp.ngOnInit();

        expect(userAccountService.query).toHaveBeenCalled();
        expect(userAccountService.addUserAccountToCollectionIfMissing).toHaveBeenCalledWith(
          userAccountCollection,
          ...additionalUserAccounts
        );
        expect(comp.userAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const question: IQuestion = { id: 456 };
        const questionCategory: IQuestionCategory = { id: 92981 };
        question.questionCategory = questionCategory;
        const createdBy: IUserAccount = { id: 13969 };
        question.createdBy = createdBy;

        activatedRoute.data = of({ question });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(question));
        expect(comp.questionCategoriesSharedCollection).toContain(questionCategory);
        expect(comp.userAccountsSharedCollection).toContain(createdBy);
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
      describe('trackQuestionCategoryById', () => {
        it('Should return tracked QuestionCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuestionCategoryById(0, entity);
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
