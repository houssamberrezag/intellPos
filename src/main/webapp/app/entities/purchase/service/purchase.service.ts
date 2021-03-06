import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchase, getPurchaseIdentifier } from '../purchase.model';

export type EntityResponseType = HttpResponse<IPurchase>;
export type EntityArrayResponseType = HttpResponse<IPurchase[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchase: IPurchase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchase);
    return this.http
      .post<IPurchase>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  create2(purchases: IPurchase[], paid: number, discountAmount: number, paymentMethod: string): Observable<EntityResponseType> {
    const copy = purchases.map(purchase => this.convertDateFromClient(purchase));
    const params: HttpParams = new HttpParams().set('paid', paid).set('discountAmount', discountAmount).set('paymentMethod', paymentMethod);
    return this.http
      .post<IPurchase>(this.resourceUrl, copy, { observe: 'response', params })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchase: IPurchase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchase);
    return this.http
      .put<IPurchase>(`${this.resourceUrl}/${getPurchaseIdentifier(purchase) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(purchase: IPurchase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchase);
    return this.http
      .patch<IPurchase>(`${this.resourceUrl}/${getPurchaseIdentifier(purchase) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchase>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchase[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findByProductId(productId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchase[]>(`${this.resourceUrl}ByProductId?productId=${productId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPurchaseToCollectionIfMissing(purchaseCollection: IPurchase[], ...purchasesToCheck: (IPurchase | null | undefined)[]): IPurchase[] {
    const purchases: IPurchase[] = purchasesToCheck.filter(isPresent);
    if (purchases.length > 0) {
      const purchaseCollectionIdentifiers = purchaseCollection.map(purchaseItem => getPurchaseIdentifier(purchaseItem)!);
      const purchasesToAdd = purchases.filter(purchaseItem => {
        const purchaseIdentifier = getPurchaseIdentifier(purchaseItem);
        if (purchaseIdentifier == null || purchaseCollectionIdentifiers.includes(purchaseIdentifier)) {
          return false;
        }
        purchaseCollectionIdentifiers.push(purchaseIdentifier);
        return true;
      });
      return [...purchasesToAdd, ...purchaseCollection];
    }
    return purchaseCollection;
  }

  getPurchasesByReference(reference: string): Observable<EntityArrayResponseType> {
    const options = { reference };
    return this.http
      .get<IPurchase[]>('api/purchasesByReference', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  totalQuantity(personId: number): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/totalQuantity?personId=${personId}`, { observe: 'body' });
  }

  findTodaySumQuantitesAndItems(): Observable<{ quantite?: number; total?: number }> {
    return this.http.get<any>(`${this.resourceUrl}/findTodaySumQuantitesAndItems`, { observe: 'body' });
  }

  protected convertDateFromClient(purchase: IPurchase): IPurchase {
    return Object.assign({}, purchase, {
      date: purchase.date?.isValid() ? purchase.date.toJSON() : undefined,
      createdAt: purchase.createdAt?.isValid() ? purchase.createdAt.toJSON() : undefined,
      updatedAt: purchase.updatedAt?.isValid() ? purchase.updatedAt.toJSON() : undefined,
      deletedAt: purchase.deletedAt?.isValid() ? purchase.deletedAt.toJSON() : undefined,
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
      res.body.forEach((purchase: IPurchase) => {
        purchase.date = purchase.date ? dayjs(purchase.date) : undefined;
        purchase.createdAt = purchase.createdAt ? dayjs(purchase.createdAt) : undefined;
        purchase.updatedAt = purchase.updatedAt ? dayjs(purchase.updatedAt) : undefined;
        purchase.deletedAt = purchase.deletedAt ? dayjs(purchase.deletedAt) : undefined;
      });
    }
    return res;
  }
}
