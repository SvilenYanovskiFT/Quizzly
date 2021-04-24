import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuizResultService } from '../service/quiz-result.service';

import { QuizResultComponent } from './quiz-result.component';

describe('Component Tests', () => {
  describe('QuizResult Management Component', () => {
    let comp: QuizResultComponent;
    let fixture: ComponentFixture<QuizResultComponent>;
    let service: QuizResultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuizResultComponent],
      })
        .overrideTemplate(QuizResultComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuizResultComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(QuizResultService);

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
      expect(comp.quizResults?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
