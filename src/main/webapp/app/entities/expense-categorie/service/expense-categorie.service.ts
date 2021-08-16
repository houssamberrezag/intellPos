import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpenseCategorie, getExpenseCategorieIdentifier } from '../expense-categorie.model';

export type EntityResponseType = HttpResponse<IExpenseCategorie>;
export type EntityArrayResponseType = HttpResponse<IExpenseCategorie[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseCategorieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expenseCategorie: IExpenseCategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseCategorie);
    return this.http
      .post<IExpenseCategorie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expenseCategorie: IExpenseCategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseCategorie);
    return this.http
      .put<IExpenseCategorie>(`${this.resourceUrl}/${getExpenseCategorieIdentifier(expenseCategorie) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(expenseCategorie: IExpenseCategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseCategorie);
    return this.http
      .patch<IExpenseCategorie>(`${this.resourceUrl}/${getExpenseCategorieIdentifier(expenseCategorie) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpenseCategorie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpenseCategorie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExpenseCategorieToCollectionIfMissing(
    expenseCategorieCollection: IExpenseCategorie[],
    ...expenseCategoriesToCheck: (IExpenseCategorie | null | undefined)[]
  ): IExpenseCategorie[] {
    const expenseCategories: IExpenseCategorie[] = expenseCategoriesToCheck.filter(isPresent);
    if (expenseCategories.length > 0) {
      const expenseCategorieCollectionIdentifiers = expenseCategorieCollection.map(
        expenseCategorieItem => getExpenseCategorieIdentifier(expenseCategorieItem)!
      );
      const expenseCategoriesToAdd = expenseCategories.filter(expenseCategorieItem => {
        const expenseCategorieIdentifier = getExpenseCategorieIdentifier(expenseCategorieItem);
        if (expenseCategorieIdentifier == null || expenseCategorieCollectionIdentifiers.includes(expenseCategorieIdentifier)) {
          return false;
        }
        expenseCategorieCollectionIdentifiers.push(expenseCategorieIdentifier);
        return true;
      });
      return [...expenseCategoriesToAdd, ...expenseCategorieCollection];
    }
    return expenseCategorieCollection;
  }

  protected convertDateFromClient(expenseCategorie: IExpenseCategorie): IExpenseCategorie {
    return Object.assign({}, expenseCategorie, {
      createdAt: expenseCategorie.createdAt?.isValid() ? expenseCategorie.createdAt.toJSON() : undefined,
      updatedAt: expenseCategorie.updatedAt?.isValid() ? expenseCategorie.updatedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((expenseCategorie: IExpenseCategorie) => {
        expenseCategorie.createdAt = expenseCategorie.createdAt ? dayjs(expenseCategorie.createdAt) : undefined;
        expenseCategorie.updatedAt = expenseCategorie.updatedAt ? dayjs(expenseCategorie.updatedAt) : undefined;
      });
    }
    return res;
  }
}
