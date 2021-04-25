import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { InvitationComponent } from './list/invitation.component';
import { InvitationDetailComponent } from './detail/invitation-detail.component';
import { InvitationUpdateComponent } from './update/invitation-update.component';
import { InvitationDeleteDialogComponent } from './delete/invitation-delete-dialog.component';
import { InvitationRoutingModule } from './route/invitation-routing.module';

@NgModule({
  imports: [SharedModule, InvitationRoutingModule],
  declarations: [InvitationComponent, InvitationDetailComponent, InvitationUpdateComponent, InvitationDeleteDialogComponent],
  entryComponents: [InvitationDeleteDialogComponent],
})
export class InvitationModule {}
