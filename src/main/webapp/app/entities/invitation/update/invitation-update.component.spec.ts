jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InvitationService } from '../service/invitation.service';
import { IInvitation, Invitation } from '../invitation.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

import { InvitationUpdateComponent } from './invitation-update.component';

describe('Component Tests', () => {
  describe('Invitation Management Update Component', () => {
    let comp: InvitationUpdateComponent;
    let fixture: ComponentFixture<InvitationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let invitationService: InvitationService;
    let userAccountService: UserAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvitationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InvitationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvitationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      invitationService = TestBed.inject(InvitationService);
      userAccountService = TestBed.inject(UserAccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call UserAccount query and add missing value', () => {
        const invitation: IInvitation = { id: 456 };
        const userAccounts: IUserAccount[] = [{ id: 36775 }];
        invitation.userAccounts = userAccounts;

        const userAccountCollection: IUserAccount[] = [{ id: 34126 }];
        spyOn(userAccountService, 'query').and.returnValue(of(new HttpResponse({ body: userAccountCollection })));
        const additionalUserAccounts = [...userAccounts];
        const expectedCollection: IUserAccount[] = [...additionalUserAccounts, ...userAccountCollection];
        spyOn(userAccountService, 'addUserAccountToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ invitation });
        comp.ngOnInit();

        expect(userAccountService.query).toHaveBeenCalled();
        expect(userAccountService.addUserAccountToCollectionIfMissing).toHaveBeenCalledWith(
          userAccountCollection,
          ...additionalUserAccounts
        );
        expect(comp.userAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const invitation: IInvitation = { id: 456 };
        const userAccounts: IUserAccount = { id: 15798 };
        invitation.userAccounts = [userAccounts];

        activatedRoute.data = of({ invitation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(invitation));
        expect(comp.userAccountsSharedCollection).toContain(userAccounts);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const invitation = { id: 123 };
        spyOn(invitationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ invitation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invitation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(invitationService.update).toHaveBeenCalledWith(invitation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const invitation = new Invitation();
        spyOn(invitationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ invitation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invitation }));
        saveSubject.complete();

        // THEN
        expect(invitationService.create).toHaveBeenCalledWith(invitation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const invitation = { id: 123 };
        spyOn(invitationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ invitation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(invitationService.update).toHaveBeenCalledWith(invitation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserAccountById', () => {
        it('Should return tracked UserAccount primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserAccountById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedUserAccount', () => {
        it('Should return option if no UserAccount is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedUserAccount(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected UserAccount for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedUserAccount(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this UserAccount is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedUserAccount(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
