import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpense, getExpenseIdentifier } from '../expense.model';

export type EntityResponseType = HttpResponse<IExpense>;
export type EntityArrayResponseType = HttpResponse<IExpense[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expense: IExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expense);
    return this.http
      .post<IExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expense: IExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expense);
    return this.http
      .put<IExpense>(`${this.resourceUrl}/${getExpenseIdentifier(expense) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(expense: IExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expense);
    return this.http
      .patch<IExpense>(`${this.resourceUrl}/${getExpenseIdentifier(expense) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExpenseToCollectionIfMissing(expenseCollection: IExpense[], ...expensesToCheck: (IExpense | null | undefined)[]): IExpense[] {
    const expenses: IExpense[] = expensesToCheck.filter(isPresent);
    if (expenses.length > 0) {
      const expenseCollectionIdentifiers = expenseCollection.map(expenseItem => getExpenseIdentifier(expenseItem)!);
      const expensesToAdd = expenses.filter(expenseItem => {
        const expenseIdentifier = getExpenseIdentifier(expenseItem);
        if (expenseIdentifier == null || expenseCollectionIdentifiers.includes(expenseIdentifier)) {
          return false;
        }
        expenseCollectionIdentifiers.push(expenseIdentifier);
        return true;
      });
      return [...expensesToAdd, ...expenseCollection];
    }
    return expenseCollection;
  }

  protected convertDateFromClient(expense: IExpense): IExpense {
    return Object.assign({}, expense, {
      createdAt: expense.createdAt?.isValid() ? expense.createdAt.toJSON() : undefined,
      updatedAt: expense.updatedAt?.isValid() ? expense.updatedAt.toJSON() : undefined,
      deletedAt: expense.deletedAt?.isValid() ? expense.deletedAt.toJSON() : undefined,
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
      res.body.forEach((expense: IExpense) => {
        expense.createdAt = expense.createdAt ? dayjs(expense.createdAt) : undefined;
        expense.updatedAt = expense.updatedAt ? dayjs(expense.updatedAt) : undefined;
        expense.deletedAt = expense.deletedAt ? dayjs(expense.deletedAt) : undefined;
      });
    }
    return res;
  }
}
