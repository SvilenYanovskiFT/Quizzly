import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizRezult, QuizRezult } from '../quiz-rezult.model';

import { QuizRezultService } from './quiz-rezult.service';

describe('Service Tests', () => {
  describe('QuizRezult Service', () => {
    let service: QuizRezultService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuizRezult;
    let expectedResult: IQuizRezult | IQuizRezult[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuizRezultService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        score: 0,
        rank: 0,
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

      it('should create a QuizRezult', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new QuizRezult()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a QuizRezult', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            score: 1,
            rank: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a QuizRezult', () => {
        const patchObject = Object.assign({}, new QuizRezult());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of QuizRezult', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            score: 1,
            rank: 1,
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

      it('should delete a QuizRezult', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuizRezultToCollectionIfMissing', () => {
        it('should add a QuizRezult to an empty array', () => {
          const quizRezult: IQuizRezult = { id: 123 };
          expectedResult = service.addQuizRezultToCollectionIfMissing([], quizRezult);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(quizRezult);
        });

        it('should not add a QuizRezult to an array that contains it', () => {
          const quizRezult: IQuizRezult = { id: 123 };
          const quizRezultCollection: IQuizRezult[] = [
            {
              ...quizRezult,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuizRezultToCollectionIfMissing(quizRezultCollection, quizRezult);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a QuizRezult to an array that doesn't contain it", () => {
          const quizRezult: IQuizRezult = { id: 123 };
          const quizRezultCollection: IQuizRezult[] = [{ id: 456 }];
          expectedResult = service.addQuizRezultToCollectionIfMissing(quizRezultCollection, quizRezult);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(quizRezult);
        });

        it('should add only unique QuizRezult to an array', () => {
          const quizRezultArray: IQuizRezult[] = [{ id: 123 }, { id: 456 }, { id: 51007 }];
          const quizRezultCollection: IQuizRezult[] = [{ id: 123 }];
          expectedResult = service.addQuizRezultToCollectionIfMissing(quizRezultCollection, ...quizRezultArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const quizRezult: IQuizRezult = { id: 123 };
          const quizRezult2: IQuizRezult = { id: 456 };
          expectedResult = service.addQuizRezultToCollectionIfMissing([], quizRezult, quizRezult2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(quizRezult);
          expect(expectedResult).toContain(quizRezult2);
        });

        it('should accept null and undefined values', () => {
          const quizRezult: IQuizRezult = { id: 123 };
          expectedResult = service.addQuizRezultToCollectionIfMissing([], null, quizRezult, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(quizRezult);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
