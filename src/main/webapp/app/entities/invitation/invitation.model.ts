import { IUserAccount } from 'app/entities/user-account/user-account.model';

export interface IInvitation {
  id?: number;
  quizCode?: string | null;
  invitedBy?: string | null;
  userAccounts?: IUserAccount[] | null;
}

export class Invitation implements IInvitation {
  constructor(
    public id?: number,
    public quizCode?: string | null,
    public invitedBy?: string | null,
    public userAccounts?: IUserAccount[] | null
  ) {}
}

export function getInvitationIdentifier(invitation: IInvitation): number | undefined {
  return invitation.id;
}
