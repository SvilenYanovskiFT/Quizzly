import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInvitation, Invitation } from '../invitation.model';

import { InvitationService } from './invitation.service';

describe('Service Tests', () => {
  describe('Invitation Service', () => {
    let service: InvitationService;
    let httpMock: HttpTestingController;
    let elemDefault: IInvitation;
    let expectedResult: IInvitation | IInvitation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InvitationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        quizCode: 'AAAAAAA',
        invitedBy: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Invitation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Invitation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Invitation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quizCode: 'BBBBBB',
            invitedBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Invitation', () => {
        const patchObject = Object.assign(
          {
            quizCode: 'BBBBBB',
            invitedBy: 'BBBBBB',
          },
          new Invitation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Invitation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quizCode: 'BBBBBB',
            invitedBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Invitation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInvitationToCollectionIfMissing', () => {
        it('should add a Invitation to an empty array', () => {
          const invitation: IInvitation = { id: 123 };
          expectedResult = service.addInvitationToCollectionIfMissing([], invitation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invitation);
        });

        it('should not add a Invitation to an array that contains it', () => {
          const invitation: IInvitation = { id: 123 };
          const invitationCollection: IInvitation[] = [
            {
              ...invitation,
            },
            { id: 456 },
          ];
          expectedResult = service.addInvitationToCollectionIfMissing(invitationCollection, invitation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Invitation to an array that doesn't contain it", () => {
          const invitation: IInvitation = { id: 123 };
          const invitationCollection: IInvitation[] = [{ id: 456 }];
          expectedResult = service.addInvitationToCollectionIfMissing(invitationCollection, invitation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invitation);
        });

        it('should add only unique Invitation to an array', () => {
          const invitationArray: IInvitation[] = [{ id: 123 }, { id: 456 }, { id: 28486 }];
          const invitationCollection: IInvitation[] = [{ id: 123 }];
          expectedResult = service.addInvitationToCollectionIfMissing(invitationCollection, ...invitationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const invitation: IInvitation = { id: 123 };
          const invitation2: IInvitation = { id: 456 };
          expectedResult = service.addInvitationToCollectionIfMissing([], invitation, invitation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invitation);
          expect(expectedResult).toContain(invitation2);
        });

        it('should accept null and undefined values', () => {
          const invitation: IInvitation = { id: 123 };
          expectedResult = service.addInvitationToCollectionIfMissing([], null, invitation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invitation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
