jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestionCategoryService } from '../service/question-category.service';
import { IQuestionCategory, QuestionCategory } from '../question-category.model';

import { QuestionCategoryUpdateComponent } from './question-category-update.component';

describe('Component Tests', () => {
  describe('QuestionCategory Management Update Component', () => {
    let comp: QuestionCategoryUpdateComponent;
    let fixture: ComponentFixture<QuestionCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let questionCategoryService: QuestionCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuestionCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      questionCategoryService = TestBed.inject(QuestionCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const questionCategory: IQuestionCategory = { id: 456 };

        activatedRoute.data = of({ questionCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(questionCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const questionCategory = { id: 123 };
        spyOn(questionCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(questionCategoryService.update).toHaveBeenCalledWith(questionCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const questionCategory = new QuestionCategory();
        spyOn(questionCategoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: questionCategory }));
        saveSubject.complete();

        // THEN
        expect(questionCategoryService.create).toHaveBeenCalledWith(questionCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const questionCategory = { id: 123 };
        spyOn(questionCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ questionCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(questionCategoryService.update).toHaveBeenCalledWith(questionCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
