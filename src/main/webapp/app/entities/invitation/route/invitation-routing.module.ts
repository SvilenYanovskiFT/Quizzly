import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvitationComponent } from '../list/invitation.component';
import { InvitationDetailComponent } from '../detail/invitation-detail.component';
import { InvitationUpdateComponent } from '../update/invitation-update.component';
import { InvitationRoutingResolveService } from './invitation-routing-resolve.service';

const invitationRoute: Routes = [
  {
    path: '',
    component: InvitationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvitationDetailComponent,
    resolve: {
      invitation: InvitationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvitationUpdateComponent,
    resolve: {
      invitation: InvitationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvitationUpdateComponent,
    resolve: {
      invitation: InvitationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(invitationRoute)],
  exports: [RouterModule],
})
export class InvitationRoutingModule {}
