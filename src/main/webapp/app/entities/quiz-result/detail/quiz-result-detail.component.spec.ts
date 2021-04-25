import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuizResultDetailComponent } from './quiz-result-detail.component';

describe('Component Tests', () => {
  describe('QuizResult Management Detail Component', () => {
    let comp: QuizResultDetailComponent;
    let fixture: ComponentFixture<QuizResultDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [QuizResultDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ quizResult: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(QuizResultDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuizResultDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load quizResult on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.quizResult).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
