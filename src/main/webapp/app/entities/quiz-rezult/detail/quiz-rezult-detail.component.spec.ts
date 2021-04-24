import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuizRezultDetailComponent } from './quiz-rezult-detail.component';

describe('Component Tests', () => {
  describe('QuizRezult Management Detail Component', () => {
    let comp: QuizRezultDetailComponent;
    let fixture: ComponentFixture<QuizRezultDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [QuizRezultDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ quizRezult: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(QuizRezultDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuizRezultDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load quizRezult on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.quizRezult).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
