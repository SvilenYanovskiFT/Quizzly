jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuizResult, QuizResult } from '../quiz-result.model';
import { QuizResultService } from '../service/quiz-result.service';

import { QuizResultRoutingResolveService } from './quiz-result-routing-resolve.service';

describe('Service Tests', () => {
  describe('QuizResult routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: QuizResultRoutingResolveService;
    let service: QuizResultService;
    let resultQuizResult: IQuizResult | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(QuizResultRoutingResolveService);
      service = TestBed.inject(QuizResultService);
      resultQuizResult = undefined;
    });

    describe('resolve', () => {
      it('should return IQuizResult returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuizResult = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuizResult).toEqual({ id: 123 });
      });

      it('should return new IQuizResult if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuizResult = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultQuizResult).toEqual(new QuizResult());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuizResult = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuizResult).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
