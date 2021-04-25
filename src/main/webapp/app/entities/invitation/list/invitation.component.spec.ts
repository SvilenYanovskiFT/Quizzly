import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InvitationService } from '../service/invitation.service';

import { InvitationComponent } from './invitation.component';

describe('Component Tests', () => {
  describe('Invitation Management Component', () => {
    let comp: InvitationComponent;
    let fixture: ComponentFixture<InvitationComponent>;
    let service: InvitationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvitationComponent],
      })
        .overrideTemplate(InvitationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvitationComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InvitationService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.invitations?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
