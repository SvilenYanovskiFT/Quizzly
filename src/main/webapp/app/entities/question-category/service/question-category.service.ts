import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionCategory, getQuestionCategoryIdentifier } from '../question-category.model';

export type EntityResponseType = HttpResponse<IQuestionCategory>;
export type EntityArrayResponseType = HttpResponse<IQuestionCategory[]>;

@Injectable({ providedIn: 'root' })
export class QuestionCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/question-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(questionCategory: IQuestionCategory): Observable<EntityResponseType> {
    return this.http.post<IQuestionCategory>(this.resourceUrl, questionCategory, { observe: 'response' });
  }

  update(questionCategory: IQuestionCategory): Observable<EntityResponseType> {
    return this.http.put<IQuestionCategory>(
      `${this.resourceUrl}/${getQuestionCategoryIdentifier(questionCategory) as number}`,
      questionCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(questionCategory: IQuestionCategory): Observable<EntityResponseType> {
    return this.http.patch<IQuestionCategory>(
      `${this.resourceUrl}/${getQuestionCategoryIdentifier(questionCategory) as number}`,
      questionCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionCategoryToCollectionIfMissing(
    questionCategoryCollection: IQuestionCategory[],
    ...questionCategoriesToCheck: (IQuestionCategory | null | undefined)[]
  ): IQuestionCategory[] {
    const questionCategories: IQuestionCategory[] = questionCategoriesToCheck.filter(isPresent);
    if (questionCategories.length > 0) {
      const questionCategoryCollectionIdentifiers = questionCategoryCollection.map(
        questionCategoryItem => getQuestionCategoryIdentifier(questionCategoryItem)!
      );
      const questionCategoriesToAdd = questionCategories.filter(questionCategoryItem => {
        const questionCategoryIdentifier = getQuestionCategoryIdentifier(questionCategoryItem);
        if (questionCategoryIdentifier == null || questionCategoryCollectionIdentifiers.includes(questionCategoryIdentifier)) {
          return false;
        }
        questionCategoryCollectionIdentifiers.push(questionCategoryIdentifier);
        return true;
      });
      return [...questionCategoriesToAdd, ...questionCategoryCollection];
    }
    return questionCategoryCollection;
  }
}
