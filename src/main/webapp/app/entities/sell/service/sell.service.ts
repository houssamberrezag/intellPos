import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISell, getSellIdentifier } from '../sell.model';

export type EntityResponseType = HttpResponse<ISell>;
export type EntityArrayResponseType = HttpResponse<ISell[]>;

@Injectable({ providedIn: 'root' })
export class SellService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sells');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sell: ISell): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sell);
    return this.http
      .post<ISell>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  create2(
    sells: ISell[],
    paid: number,
    shippingCost: number,
    discountAmount: number,
    paymentMethod: string
  ): Observable<EntityResponseType> {
    const copy = sells.map(sell => this.convertDateFromClient(sell));
    const params: HttpParams = new HttpParams()
      .set('paid', paid)
      .set('shippingCost', shippingCost)
      .set('discountAmount', discountAmount)
      .set('paymentMethod', paymentMethod);
    return this.http
      .post<ISell>(this.resourceUrl, copy, { observe: 'response', params })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  createPos(
    sells: ISell[],
    paid: number,
    changeAmount: number,
    discountAmount: number,
    paymentMethod: string
  ): Observable<EntityResponseType> {
    const copy = sells.map(sell => this.convertDateFromClient(sell));
    const params: HttpParams = new HttpParams()
      .set('paid', paid)
      .set('changeAmount', changeAmount)
      .set('discountAmount', discountAmount)
      .set('paymentMethod', paymentMethod);
    return this.http
      .post<ISell>(this.resourceUrl + '/pos', copy, { observe: 'response', params })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  returnSell(sellsReturnWithQuantity: string[], transactionId: number): Observable<EntityArrayResponseType> {
    const params: HttpParams = new HttpParams().set('transactionId', transactionId);
    return this.http
      .post<ISell[]>(this.resourceUrl + '/return', sellsReturnWithQuantity, { observe: 'response', params })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getSellsByReference(reference: string): Observable<EntityArrayResponseType> {
    const options = { reference };
    return this.http
      .get<ISell[]>('api/sellsByReference', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  update(sell: ISell): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sell);
    return this.http
      .put<ISell>(`${this.resourceUrl}/${getSellIdentifier(sell) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sell: ISell): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sell);
    return this.http
      .patch<ISell>(`${this.resourceUrl}/${getSellIdentifier(sell) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISell>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISell[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSellToCollectionIfMissing(sellCollection: ISell[], ...sellsToCheck: (ISell | null | undefined)[]): ISell[] {
    const sells: ISell[] = sellsToCheck.filter(isPresent);
    if (sells.length > 0) {
      const sellCollectionIdentifiers = sellCollection.map(sellItem => getSellIdentifier(sellItem)!);
      const sellsToAdd = sells.filter(sellItem => {
        const sellIdentifier = getSellIdentifier(sellItem);
        if (sellIdentifier == null || sellCollectionIdentifiers.includes(sellIdentifier)) {
          return false;
        }
        sellCollectionIdentifiers.push(sellIdentifier);
        return true;
      });
      return [...sellsToAdd, ...sellCollection];
    }
    return sellCollection;
  }

  protected convertDateFromClient(sell: ISell): ISell {
    return Object.assign({}, sell, {
      date: sell.date?.isValid() ? sell.date.toJSON() : undefined,
      createdAt: sell.createdAt?.isValid() ? sell.createdAt.toJSON() : undefined,
      updatedAt: sell.updatedAt?.isValid() ? sell.updatedAt.toJSON() : undefined,
      deletedAt: sell.deletedAt?.isValid() ? sell.deletedAt.toJSON() : undefined,
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
      res.body.forEach((sell: ISell) => {
        sell.date = sell.date ? dayjs(sell.date) : undefined;
        sell.createdAt = sell.createdAt ? dayjs(sell.createdAt) : undefined;
        sell.updatedAt = sell.updatedAt ? dayjs(sell.updatedAt) : undefined;
        sell.deletedAt = sell.deletedAt ? dayjs(sell.deletedAt) : undefined;
      });
    }
    return res;
  }
}
