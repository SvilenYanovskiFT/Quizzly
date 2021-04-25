import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvitation, getInvitationIdentifier } from '../invitation.model';

export type EntityResponseType = HttpResponse<IInvitation>;
export type EntityArrayResponseType = HttpResponse<IInvitation[]>;

@Injectable({ providedIn: 'root' })
export class InvitationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/invitations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(invitation: IInvitation): Observable<EntityResponseType> {
    return this.http.post<IInvitation>(this.resourceUrl, invitation, { observe: 'response' });
  }

  update(invitation: IInvitation): Observable<EntityResponseType> {
    return this.http.put<IInvitation>(`${this.resourceUrl}/${getInvitationIdentifier(invitation) as number}`, invitation, {
      observe: 'response',
    });
  }

  partialUpdate(invitation: IInvitation): Observable<EntityResponseType> {
    return this.http.patch<IInvitation>(`${this.resourceUrl}/${getInvitationIdentifier(invitation) as number}`, invitation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInvitation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInvitation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvitationToCollectionIfMissing(
    invitationCollection: IInvitation[],
    ...invitationsToCheck: (IInvitation | null | undefined)[]
  ): IInvitation[] {
    const invitations: IInvitation[] = invitationsToCheck.filter(isPresent);
    if (invitations.length > 0) {
      const invitationCollectionIdentifiers = invitationCollection.map(invitationItem => getInvitationIdentifier(invitationItem)!);
      const invitationsToAdd = invitations.filter(invitationItem => {
        const invitationIdentifier = getInvitationIdentifier(invitationItem);
        if (invitationIdentifier == null || invitationCollectionIdentifiers.includes(invitationIdentifier)) {
          return false;
        }
        invitationCollectionIdentifiers.push(invitationIdentifier);
        return true;
      });
      return [...invitationsToAdd, ...invitationCollection];
    }
    return invitationCollection;
  }
}
