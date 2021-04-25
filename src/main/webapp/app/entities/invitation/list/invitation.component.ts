import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvitation } from '../invitation.model';
import { InvitationService } from '../service/invitation.service';
import { InvitationDeleteDialogComponent } from '../delete/invitation-delete-dialog.component';

@Component({
  selector: 'jhi-invitation',
  templateUrl: './invitation.component.html',
})
export class InvitationComponent implements OnInit {
  invitations?: IInvitation[];
  isLoading = false;

  constructor(protected invitationService: InvitationService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.invitationService.query().subscribe(
      (res: HttpResponse<IInvitation[]>) => {
        this.isLoading = false;
        this.invitations = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInvitation): number {
    return item.id!;
  }

  delete(invitation: IInvitation): void {
    const modalRef = this.modalService.open(InvitationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.invitation = invitation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
