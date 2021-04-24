jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuestionCategory, QuestionCategory } from '../question-category.model';
import { QuestionCategoryService } from '../service/question-category.service';

import { QuestionCategoryRoutingResolveService } from './question-category-routing-resolve.service';

describe('Service Tests', () => {
  describe('QuestionCategory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: QuestionCategoryRoutingResolveService;
    let service: QuestionCategoryService;
    let resultQuestionCategory: IQuestionCategory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(QuestionCategoryRoutingResolveService);
      service = TestBed.inject(QuestionCategoryService);
      resultQuestionCategory = undefined;
    });

    describe('resolve', () => {
      it('should return IQuestionCategory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuestionCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuestionCategory).toEqual({ id: 123 });
      });

      it('should return new IQuestionCategory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuestionCategory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultQuestionCategory).toEqual(new QuestionCategory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuestionCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuestionCategory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
