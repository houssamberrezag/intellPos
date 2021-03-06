import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransaction, getTransactionIdentifier } from '../transaction.model';

export type EntityResponseType = HttpResponse<ITransaction>;
export type EntityArrayResponseType = HttpResponse<ITransaction[]>;

@Injectable({ providedIn: 'root' })
export class TransactionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transactions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transaction: ITransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .post<ITransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(transaction: ITransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .put<ITransaction>(`${this.resourceUrl}/${getTransactionIdentifier(transaction) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(transaction: ITransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .patch<ITransaction>(`${this.resourceUrl}/${getTransactionIdentifier(transaction) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByRef(ref: string): Observable<EntityResponseType> {
    return this.http
      .get<ITransaction>(`api/transactionByRef?ref=${ref}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  purchases(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl + '/purchases', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  sells(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl + '/sells', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  todayPurchases(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl + '/purchases/today', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  todaySells(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl + '/sells/today', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  purchasesByPersonId(personId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(`${this.resourceUrl}/purchasesByPersonId?personId=${personId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  sellsByPersonId(personId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(`${this.resourceUrl}/sellsByPersonId?personId=${personId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  transactionsSellByProductId(productId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl + '/sellByProductId?productId=' + productId.toString(), {
        params: options,
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  transactionsPurchaseByProductId(productId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl + '/purchaseByProductId?productId=' + productId.toString(), {
        params: options,
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  countByPersonId(personId: number): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/countByPersonId?personId=${personId}`, { observe: 'body' });
  }

  countByProductId(productId: number): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/countByProductId?productId=${productId}`, { observe: 'body' });
  }

  totalAmountByPersonId(personId: number): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/totalAmountByPersonId?personId=${personId}`, { observe: 'body' });
  }

  sellResume(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/sellResume`, { observe: 'body' });
  }

  purchasesResume(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/purchasesResume`, { observe: 'body' });
  }

  sellTodayResume(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/sellTodayResume`, { observe: 'body' });
  }

  purchasesTodayResume(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/purchasesTodayResume`, { observe: 'body' });
  }

  addTransactionToCollectionIfMissing(
    transactionCollection: ITransaction[],
    ...transactionsToCheck: (ITransaction | null | undefined)[]
  ): ITransaction[] {
    const transactions: ITransaction[] = transactionsToCheck.filter(isPresent);
    if (transactions.length > 0) {
      const transactionCollectionIdentifiers = transactionCollection.map(transactionItem => getTransactionIdentifier(transactionItem)!);
      const transactionsToAdd = transactions.filter(transactionItem => {
        const transactionIdentifier = getTransactionIdentifier(transactionItem);
        if (transactionIdentifier == null || transactionCollectionIdentifiers.includes(transactionIdentifier)) {
          return false;
        }
        transactionCollectionIdentifiers.push(transactionIdentifier);
        return true;
      });
      return [...transactionsToAdd, ...transactionCollection];
    }
    return transactionCollection;
  }

  protected convertDateFromClient(transaction: ITransaction): ITransaction {
    return Object.assign({}, transaction, {
      date: transaction.date?.isValid() ? transaction.date.toJSON() : undefined,
      createdAt: transaction.createdAt?.isValid() ? transaction.createdAt.toJSON() : undefined,
      updatedAt: transaction.updatedAt?.isValid() ? transaction.updatedAt.toJSON() : undefined,
      deletedAt: transaction.deletedAt?.isValid() ? transaction.deletedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.deletedAt = res.body.deletedAt ? dayjs(res.body.deletedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((transaction: ITransaction) => {
        transaction.date = transaction.date ? dayjs(transaction.date) : undefined;
        transaction.createdAt = transaction.createdAt ? dayjs(transaction.createdAt) : undefined;
        transaction.updatedAt = transaction.updatedAt ? dayjs(transaction.updatedAt) : undefined;
        transaction.deletedAt = transaction.deletedAt ? dayjs(transaction.deletedAt) : undefined;
      });
    }
    return res;
  }
}
