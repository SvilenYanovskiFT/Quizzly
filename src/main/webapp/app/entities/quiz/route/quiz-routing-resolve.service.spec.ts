jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuiz, Quiz } from '../quiz.model';
import { QuizService } from '../service/quiz.service';

import { QuizRoutingResolveService } from './quiz-routing-resolve.service';

describe('Service Tests', () => {
  describe('Quiz routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: QuizRoutingResolveService;
    let service: QuizService;
    let resultQuiz: IQuiz | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(QuizRoutingResolveService);
      service = TestBed.inject(QuizService);
      resultQuiz = undefined;
    });

    describe('resolve', () => {
      it('should return IQuiz returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuiz = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuiz).toEqual({ id: 123 });
      });

      it('should return new IQuiz if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuiz = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultQuiz).toEqual(new Quiz());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuiz = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuiz).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
