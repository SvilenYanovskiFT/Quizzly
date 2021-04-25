import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInvitation, Invitation } from '../invitation.model';
import { InvitationService } from '../service/invitation.service';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';

@Component({
  selector: 'jhi-invitation-update',
  templateUrl: './invitation-update.component.html',
})
export class InvitationUpdateComponent implements OnInit {
  isSaving = false;

  userAccountsSharedCollection: IUserAccount[] = [];

  editForm = this.fb.group({
    id: [],
    quizCode: [],
    invitedBy: [],
    userAccounts: [],
  });

  constructor(
    protected invitationService: InvitationService,
    protected userAccountService: UserAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invitation }) => {
      this.updateForm(invitation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invitation = this.createFromForm();
    if (invitation.id !== undefined) {
      this.subscribeToSaveResponse(this.invitationService.update(invitation));
    } else {
      this.subscribeToSaveResponse(this.invitationService.create(invitation));
    }
  }

  trackUserAccountById(index: number, item: IUserAccount): number {
    return item.id!;
  }

  getSelectedUserAccount(option: IUserAccount, selectedVals?: IUserAccount[]): IUserAccount {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvitation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(invitation: IInvitation): void {
    this.editForm.patchValue({
      id: invitation.id,
      quizCode: invitation.quizCode,
      invitedBy: invitation.invitedBy,
      userAccounts: invitation.userAccounts,
    });

    this.userAccountsSharedCollection = this.userAccountService.addUserAccountToCollectionIfMissing(
      this.userAccountsSharedCollection,
      ...(invitation.userAccounts ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userAccountService
      .query()
      .pipe(map((res: HttpResponse<IUserAccount[]>) => res.body ?? []))
      .pipe(
        map((userAccounts: IUserAccount[]) =>
          this.userAccountService.addUserAccountToCollectionIfMissing(userAccounts, ...(this.editForm.get('userAccounts')!.value ?? []))
        )
      )
      .subscribe((userAccounts: IUserAccount[]) => (this.userAccountsSharedCollection = userAccounts));
  }

  protected createFromForm(): IInvitation {
    return {
      ...new Invitation(),
      id: this.editForm.get(['id'])!.value,
      quizCode: this.editForm.get(['quizCode'])!.value,
      invitedBy: this.editForm.get(['invitedBy'])!.value,
      userAccounts: this.editForm.get(['userAccounts'])!.value,
    };
  }
}
