import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvitationDetailComponent } from './invitation-detail.component';

describe('Component Tests', () => {
  describe('Invitation Management Detail Component', () => {
    let comp: InvitationDetailComponent;
    let fixture: ComponentFixture<InvitationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InvitationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ invitation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InvitationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvitationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load invitation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.invitation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
