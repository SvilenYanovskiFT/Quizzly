import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuizService } from '../service/quiz.service';

import { QuizComponent } from './quiz.component';

describe('Component Tests', () => {
  describe('Quiz Management Component', () => {
    let comp: QuizComponent;
    let fixture: ComponentFixture<QuizComponent>;
    let service: QuizService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuizComponent],
      })
        .overrideTemplate(QuizComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuizComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(QuizService);

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
      expect(comp.quizzes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
