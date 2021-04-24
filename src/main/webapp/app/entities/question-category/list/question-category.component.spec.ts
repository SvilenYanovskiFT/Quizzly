import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuestionCategoryService } from '../service/question-category.service';

import { QuestionCategoryComponent } from './question-category.component';

describe('Component Tests', () => {
  describe('QuestionCategory Management Component', () => {
    let comp: QuestionCategoryComponent;
    let fixture: ComponentFixture<QuestionCategoryComponent>;
    let service: QuestionCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuestionCategoryComponent],
      })
        .overrideTemplate(QuestionCategoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionCategoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(QuestionCategoryService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.questionCategories?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
