import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReturnTransaction, getReturnTransactionIdentifier } from '../return-transaction.model';

export type EntityResponseType = HttpResponse<IReturnTransaction>;
export type EntityArrayResponseType = HttpResponse<IReturnTransaction[]>;

@Injectable({ providedIn: 'root' })
export class ReturnTransactionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/return-transactions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(returnTransaction: IReturnTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(returnTransaction);
    return this.http
      .post<IReturnTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(returnTransaction: IReturnTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(returnTransaction);
    return this.http
      .put<IReturnTransaction>(`${this.resourceUrl}/${getReturnTransactionIdentifier(returnTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(returnTransaction: IReturnTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(returnTransaction);
    return this.http
      .patch<IReturnTransaction>(`${this.resourceUrl}/${getReturnTransactionIdentifier(returnTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReturnTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReturnTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReturnTransactionToCollectionIfMissing(
    returnTransactionCollection: IReturnTransaction[],
    ...returnTransactionsToCheck: (IReturnTransaction | null | undefined)[]
  ): IReturnTransaction[] {
    const returnTransactions: IReturnTransaction[] = returnTransactionsToCheck.filter(isPresent);
    if (returnTransactions.length > 0) {
      const returnTransactionCollectionIdentifiers = returnTransactionCollection.map(
        returnTransactionItem => getReturnTransactionIdentifier(returnTransactionItem)!
      );
      const returnTransactionsToAdd = returnTransactions.filter(returnTransactionItem => {
        const returnTransactionIdentifier = getReturnTransactionIdentifier(returnTransactionItem);
        if (returnTransactionIdentifier == null || returnTransactionCollectionIdentifiers.includes(returnTransactionIdentifier)) {
          return false;
        }
        returnTransactionCollectionIdentifiers.push(returnTransactionIdentifier);
        return true;
      });
      return [...returnTransactionsToAdd, ...returnTransactionCollection];
    }
    return returnTransactionCollection;
  }

  protected convertDateFromClient(returnTransaction: IReturnTransaction): IReturnTransaction {
    return Object.assign({}, returnTransaction, {
      createdAt: returnTransaction.createdAt?.isValid() ? returnTransaction.createdAt.toJSON() : undefined,
      updatedAt: returnTransaction.updatedAt?.isValid() ? returnTransaction.updatedAt.toJSON() : undefined,
      deletedAt: returnTransaction.deletedAt?.isValid() ? returnTransaction.deletedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.deletedAt = res.body.deletedAt ? dayjs(res.body.deletedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((returnTransaction: IReturnTransaction) => {
        returnTransaction.createdAt = returnTransaction.createdAt ? dayjs(returnTransaction.createdAt) : undefined;
        returnTransaction.updatedAt = returnTransaction.updatedAt ? dayjs(returnTransaction.updatedAt) : undefined;
        returnTransaction.deletedAt = returnTransaction.deletedAt ? dayjs(returnTransaction.deletedAt) : undefined;
      });
    }
    return res;
  }
}
