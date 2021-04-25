import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionCategory, QuestionCategory } from '../question-category.model';

import { QuestionCategoryService } from './question-category.service';

describe('Service Tests', () => {
  describe('QuestionCategory Service', () => {
    let service: QuestionCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuestionCategory;
    let expectedResult: IQuestionCategory | IQuestionCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuestionCategoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a QuestionCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new QuestionCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a QuestionCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a QuestionCategory', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new QuestionCategory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of QuestionCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a QuestionCategory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuestionCategoryToCollectionIfMissing', () => {
        it('should add a QuestionCategory to an empty array', () => {
          const questionCategory: IQuestionCategory = { id: 123 };
          expectedResult = service.addQuestionCategoryToCollectionIfMissing([], questionCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionCategory);
        });

        it('should not add a QuestionCategory to an array that contains it', () => {
          const questionCategory: IQuestionCategory = { id: 123 };
          const questionCategoryCollection: IQuestionCategory[] = [
            {
              ...questionCategory,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuestionCategoryToCollectionIfMissing(questionCategoryCollection, questionCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a QuestionCategory to an array that doesn't contain it", () => {
          const questionCategory: IQuestionCategory = { id: 123 };
          const questionCategoryCollection: IQuestionCategory[] = [{ id: 456 }];
          expectedResult = service.addQuestionCategoryToCollectionIfMissing(questionCategoryCollection, questionCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionCategory);
        });

        it('should add only unique QuestionCategory to an array', () => {
          const questionCategoryArray: IQuestionCategory[] = [{ id: 123 }, { id: 456 }, { id: 19531 }];
          const questionCategoryCollection: IQuestionCategory[] = [{ id: 123 }];
          expectedResult = service.addQuestionCategoryToCollectionIfMissing(questionCategoryCollection, ...questionCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const questionCategory: IQuestionCategory = { id: 123 };
          const questionCategory2: IQuestionCategory = { id: 456 };
          expectedResult = service.addQuestionCategoryToCollectionIfMissing([], questionCategory, questionCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(questionCategory);
          expect(expectedResult).toContain(questionCategory2);
        });

        it('should accept null and undefined values', () => {
          const questionCategory: IQuestionCategory = { id: 123 };
          expectedResult = service.addQuestionCategoryToCollectionIfMissing([], null, questionCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(questionCategory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
