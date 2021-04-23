import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuizRezultService } from '../service/quiz-rezult.service';

import { QuizRezultComponent } from './quiz-rezult.component';

describe('Component Tests', () => {
  describe('QuizRezult Management Component', () => {
    let comp: QuizRezultComponent;
    let fixture: ComponentFixture<QuizRezultComponent>;
    let service: QuizRezultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuizRezultComponent],
      })
        .overrideTemplate(QuizRezultComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuizRezultComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(QuizRezultService);

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
      expect(comp.quizRezults?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
