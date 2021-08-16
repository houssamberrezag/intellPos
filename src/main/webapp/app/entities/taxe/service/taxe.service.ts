import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITaxe, getTaxeIdentifier } from '../taxe.model';

export type EntityResponseType = HttpResponse<ITaxe>;
export type EntityArrayResponseType = HttpResponse<ITaxe[]>;

@Injectable({ providedIn: 'root' })
export class TaxeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/taxes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(taxe: ITaxe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxe);
    return this.http
      .post<ITaxe>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(taxe: ITaxe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxe);
    return this.http
      .put<ITaxe>(`${this.resourceUrl}/${getTaxeIdentifier(taxe) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(taxe: ITaxe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxe);
    return this.http
      .patch<ITaxe>(`${this.resourceUrl}/${getTaxeIdentifier(taxe) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITaxe>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaxe[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTaxeToCollectionIfMissing(taxeCollection: ITaxe[], ...taxesToCheck: (ITaxe | null | undefined)[]): ITaxe[] {
    const taxes: ITaxe[] = taxesToCheck.filter(isPresent);
    if (taxes.length > 0) {
      const taxeCollectionIdentifiers = taxeCollection.map(taxeItem => getTaxeIdentifier(taxeItem)!);
      const taxesToAdd = taxes.filter(taxeItem => {
        const taxeIdentifier = getTaxeIdentifier(taxeItem);
        if (taxeIdentifier == null || taxeCollectionIdentifiers.includes(taxeIdentifier)) {
          return false;
        }
        taxeCollectionIdentifiers.push(taxeIdentifier);
        return true;
      });
      return [...taxesToAdd, ...taxeCollection];
    }
    return taxeCollection;
  }

  protected convertDateFromClient(taxe: ITaxe): ITaxe {
    return Object.assign({}, taxe, {
      createdAt: taxe.createdAt?.isValid() ? taxe.createdAt.toJSON() : undefined,
      updatedAt: taxe.updatedAt?.isValid() ? taxe.updatedAt.toJSON() : undefined,
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
      res.body.forEach((taxe: ITaxe) => {
        taxe.createdAt = taxe.createdAt ? dayjs(taxe.createdAt) : undefined;
        taxe.updatedAt = taxe.updatedAt ? dayjs(taxe.updatedAt) : undefined;
      });
    }
    return res;
  }
}
