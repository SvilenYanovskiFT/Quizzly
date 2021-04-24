import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizRezult, getQuizRezultIdentifier } from '../quiz-rezult.model';

export type EntityResponseType = HttpResponse<IQuizRezult>;
export type EntityArrayResponseType = HttpResponse<IQuizRezult[]>;

@Injectable({ providedIn: 'root' })
export class QuizRezultService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/quiz-rezults');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(quizRezult: IQuizRezult): Observable<EntityResponseType> {
    return this.http.post<IQuizRezult>(this.resourceUrl, quizRezult, { observe: 'response' });
  }

  update(quizRezult: IQuizRezult): Observable<EntityResponseType> {
    return this.http.put<IQuizRezult>(`${this.resourceUrl}/${getQuizRezultIdentifier(quizRezult) as number}`, quizRezult, {
      observe: 'response',
    });
  }

  partialUpdate(quizRezult: IQuizRezult): Observable<EntityResponseType> {
    return this.http.patch<IQuizRezult>(`${this.resourceUrl}/${getQuizRezultIdentifier(quizRezult) as number}`, quizRezult, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuizRezult>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuizRezult[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuizRezultToCollectionIfMissing(
    quizRezultCollection: IQuizRezult[],
    ...quizRezultsToCheck: (IQuizRezult | null | undefined)[]
  ): IQuizRezult[] {
    const quizRezults: IQuizRezult[] = quizRezultsToCheck.filter(isPresent);
    if (quizRezults.length > 0) {
      const quizRezultCollectionIdentifiers = quizRezultCollection.map(quizRezultItem => getQuizRezultIdentifier(quizRezultItem)!);
      const quizRezultsToAdd = quizRezults.filter(quizRezultItem => {
        const quizRezultIdentifier = getQuizRezultIdentifier(quizRezultItem);
        if (quizRezultIdentifier == null || quizRezultCollectionIdentifiers.includes(quizRezultIdentifier)) {
          return false;
        }
        quizRezultCollectionIdentifiers.push(quizRezultIdentifier);
        return true;
      });
      return [...quizRezultsToAdd, ...quizRezultCollection];
    }
    return quizRezultCollection;
  }
}
