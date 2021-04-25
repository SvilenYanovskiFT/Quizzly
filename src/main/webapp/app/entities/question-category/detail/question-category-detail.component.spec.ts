import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionCategoryDetailComponent } from './question-category-detail.component';

describe('Component Tests', () => {
  describe('QuestionCategory Management Detail Component', () => {
    let comp: QuestionCategoryDetailComponent;
    let fixture: ComponentFixture<QuestionCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [QuestionCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ questionCategory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(QuestionCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuestionCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load questionCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.questionCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
