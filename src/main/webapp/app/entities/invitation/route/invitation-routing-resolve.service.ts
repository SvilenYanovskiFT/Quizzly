import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvitation, Invitation } from '../invitation.model';
import { InvitationService } from '../service/invitation.service';

@Injectable({ providedIn: 'root' })
export class InvitationRoutingResolveService implements Resolve<IInvitation> {
  constructor(protected service: InvitationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvitation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((invitation: HttpResponse<Invitation>) => {
          if (invitation.body) {
            return of(invitation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Invitation());
  }
}
