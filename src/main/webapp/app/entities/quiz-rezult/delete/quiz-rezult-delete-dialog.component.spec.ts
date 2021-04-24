jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { QuizRezultService } from '../service/quiz-rezult.service';

import { QuizRezultDeleteDialogComponent } from './quiz-rezult-delete-dialog.component';

describe('Component Tests', () => {
  describe('QuizRezult Management Delete Component', () => {
    let comp: QuizRezultDeleteDialogComponent;
    let fixture: ComponentFixture<QuizRezultDeleteDialogComponent>;
    let service: QuizRezultService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuizRezultDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(QuizRezultDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuizRezultDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(QuizRezultService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
