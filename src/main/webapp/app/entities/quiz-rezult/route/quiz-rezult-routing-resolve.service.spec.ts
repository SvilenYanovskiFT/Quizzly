jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuizRezult, QuizRezult } from '../quiz-rezult.model';
import { QuizRezultService } from '../service/quiz-rezult.service';

import { QuizRezultRoutingResolveService } from './quiz-rezult-routing-resolve.service';

describe('Service Tests', () => {
  describe('QuizRezult routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: QuizRezultRoutingResolveService;
    let service: QuizRezultService;
    let resultQuizRezult: IQuizRezult | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(QuizRezultRoutingResolveService);
      service = TestBed.inject(QuizRezultService);
      resultQuizRezult = undefined;
    });

    describe('resolve', () => {
      it('should return IQuizRezult returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuizRezult = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuizRezult).toEqual({ id: 123 });
      });

      it('should return new IQuizRezult if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuizRezult = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultQuizRezult).toEqual(new QuizRezult());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuizRezult = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuizRezult).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
