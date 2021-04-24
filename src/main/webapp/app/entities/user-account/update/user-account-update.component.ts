import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserAccount, UserAccount } from '../user-account.model';
import { UserAccountService } from '../service/user-account.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-account-update',
  templateUrl: './user-account-update.component.html',
})
export class UserAccountUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    rank: [],
    quizesTaken: [],
    quizzesCreated: [],
    user: [],
  });

  constructor(
    protected userAccountService: UserAccountService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAccount }) => {
      this.updateForm(userAccount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAccount = this.createFromForm();
    if (userAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.userAccountService.update(userAccount));
    } else {
      this.subscribeToSaveResponse(this.userAccountService.create(userAccount));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAccount>>): void {
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

  protected updateForm(userAccount: IUserAccount): void {
    this.editForm.patchValue({
      id: userAccount.id,
      rank: userAccount.rank,
      quizesTaken: userAccount.quizesTaken,
      quizzesCreated: userAccount.quizzesCreated,
      user: userAccount.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userAccount.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUserAccount {
    return {
      ...new UserAccount(),
      id: this.editForm.get(['id'])!.value,
      rank: this.editForm.get(['rank'])!.value,
      quizesTaken: this.editForm.get(['quizesTaken'])!.value,
      quizzesCreated: this.editForm.get(['quizzesCreated'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
