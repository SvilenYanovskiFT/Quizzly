jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInvitation, Invitation } from '../invitation.model';
import { InvitationService } from '../service/invitation.service';

import { InvitationRoutingResolveService } from './invitation-routing-resolve.service';

describe('Service Tests', () => {
  describe('Invitation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InvitationRoutingResolveService;
    let service: InvitationService;
    let resultInvitation: IInvitation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InvitationRoutingResolveService);
      service = TestBed.inject(InvitationService);
      resultInvitation = undefined;
    });

    describe('resolve', () => {
      it('should return IInvitation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvitation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvitation).toEqual({ id: 123 });
      });

      it('should return new IInvitation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvitation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInvitation).toEqual(new Invitation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvitation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvitation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
