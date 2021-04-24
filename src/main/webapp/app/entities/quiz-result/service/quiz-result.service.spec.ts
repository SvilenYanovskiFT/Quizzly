import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizResult, QuizResult } from '../quiz-result.model';

import { QuizResultService } from './quiz-result.service';

describe('Service Tests', () => {
  describe('QuizResult Service', () => {
    let service: QuizResultService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuizResult;
    let expectedResult: IQuizResult | IQuizResult[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuizResultService);
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

      it('should create a QuizResult', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new QuizResult()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a QuizResult', () => {
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

      it('should partial update a QuizResult', () => {
        const patchObject = Object.assign(
          {
            score: 1,
          },
          new QuizResult()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of QuizResult', () => {
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

      it('should delete a QuizResult', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuizResultToCollectionIfMissing', () => {
        it('should add a QuizResult to an empty array', () => {
          const quizResult: IQuizResult = { id: 123 };
          expectedResult = service.addQuizResultToCollectionIfMissing([], quizResult);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(quizResult);
        });

        it('should not add a QuizResult to an array that contains it', () => {
          const quizResult: IQuizResult = { id: 123 };
          const quizResultCollection: IQuizResult[] = [
            {
              ...quizResult,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuizResultToCollectionIfMissing(quizResultCollection, quizResult);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a QuizResult to an array that doesn't contain it", () => {
          const quizResult: IQuizResult = { id: 123 };
          const quizResultCollection: IQuizResult[] = [{ id: 456 }];
          expectedResult = service.addQuizResultToCollectionIfMissing(quizResultCollection, quizResult);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(quizResult);
        });

        it('should add only unique QuizResult to an array', () => {
          const quizResultArray: IQuizResult[] = [{ id: 123 }, { id: 456 }, { id: 35926 }];
          const quizResultCollection: IQuizResult[] = [{ id: 123 }];
          expectedResult = service.addQuizResultToCollectionIfMissing(quizResultCollection, ...quizResultArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const quizResult: IQuizResult = { id: 123 };
          const quizResult2: IQuizResult = { id: 456 };
          expectedResult = service.addQuizResultToCollectionIfMissing([], quizResult, quizResult2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(quizResult);
          expect(expectedResult).toContain(quizResult2);
        });

        it('should accept null and undefined values', () => {
          const quizResult: IQuizResult = { id: 123 };
          expectedResult = service.addQuizResultToCollectionIfMissing([], null, quizResult, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(quizResult);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
