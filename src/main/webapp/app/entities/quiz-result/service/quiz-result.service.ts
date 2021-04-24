import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizResult, getQuizResultIdentifier } from '../quiz-result.model';

export type EntityResponseType = HttpResponse<IQuizResult>;
export type EntityArrayResponseType = HttpResponse<IQuizResult[]>;

@Injectable({ providedIn: 'root' })
export class QuizResultService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/quiz-results');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(quizResult: IQuizResult): Observable<EntityResponseType> {
    return this.http.post<IQuizResult>(this.resourceUrl, quizResult, { observe: 'response' });
  }

  update(quizResult: IQuizResult): Observable<EntityResponseType> {
    return this.http.put<IQuizResult>(`${this.resourceUrl}/${getQuizResultIdentifier(quizResult) as number}`, quizResult, {
      observe: 'response',
    });
  }

  partialUpdate(quizResult: IQuizResult): Observable<EntityResponseType> {
    return this.http.patch<IQuizResult>(`${this.resourceUrl}/${getQuizResultIdentifier(quizResult) as number}`, quizResult, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuizResult>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuizResult[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuizResultToCollectionIfMissing(
    quizResultCollection: IQuizResult[],
    ...quizResultsToCheck: (IQuizResult | null | undefined)[]
  ): IQuizResult[] {
    const quizResults: IQuizResult[] = quizResultsToCheck.filter(isPresent);
    if (quizResults.length > 0) {
      const quizResultCollectionIdentifiers = quizResultCollection.map(quizResultItem => getQuizResultIdentifier(quizResultItem)!);
      const quizResultsToAdd = quizResults.filter(quizResultItem => {
        const quizResultIdentifier = getQuizResultIdentifier(quizResultItem);
        if (quizResultIdentifier == null || quizResultCollectionIdentifiers.includes(quizResultIdentifier)) {
          return false;
        }
        quizResultCollectionIdentifiers.push(quizResultIdentifier);
        return true;
      });
      return [...quizResultsToAdd, ...quizResultCollection];
    }
    return quizResultCollection;
  }
}
