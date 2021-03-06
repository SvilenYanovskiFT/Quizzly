jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuestionAnswer, QuestionAnswer } from '../question-answer.model';
import { QuestionAnswerService } from '../service/question-answer.service';

import { QuestionAnswerRoutingResolveService } from './question-answer-routing-resolve.service';

describe('Service Tests', () => {
  describe('QuestionAnswer routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: QuestionAnswerRoutingResolveService;
    let service: QuestionAnswerService;
    let resultQuestionAnswer: IQuestionAnswer | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(QuestionAnswerRoutingResolveService);
      service = TestBed.inject(QuestionAnswerService);
      resultQuestionAnswer = undefined;
    });

    describe('resolve', () => {
      it('should return IQuestionAnswer returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuestionAnswer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuestionAnswer).toEqual({ id: 123 });
      });

      it('should return new IQuestionAnswer if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuestionAnswer = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultQuestionAnswer).toEqual(new QuestionAnswer());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuestionAnswer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuestionAnswer).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
