import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvitation } from '../invitation.model';
import { InvitationService } from '../service/invitation.service';

@Component({
  templateUrl: './invitation-delete-dialog.component.html',
})
export class InvitationDeleteDialogComponent {
  invitation?: IInvitation;

  constructor(protected invitationService: InvitationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invitationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
