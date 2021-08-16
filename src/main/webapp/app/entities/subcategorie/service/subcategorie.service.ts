import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubcategorie, getSubcategorieIdentifier } from '../subcategorie.model';

export type EntityResponseType = HttpResponse<ISubcategorie>;
export type EntityArrayResponseType = HttpResponse<ISubcategorie[]>;

@Injectable({ providedIn: 'root' })
export class SubcategorieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subcategories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(subcategorie: ISubcategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subcategorie);
    return this.http
      .post<ISubcategorie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(subcategorie: ISubcategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subcategorie);
    return this.http
      .put<ISubcategorie>(`${this.resourceUrl}/${getSubcategorieIdentifier(subcategorie) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(subcategorie: ISubcategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subcategorie);
    return this.http
      .patch<ISubcategorie>(`${this.resourceUrl}/${getSubcategorieIdentifier(subcategorie) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISubcategorie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubcategorie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubcategorieToCollectionIfMissing(
    subcategorieCollection: ISubcategorie[],
    ...subcategoriesToCheck: (ISubcategorie | null | undefined)[]
  ): ISubcategorie[] {
    const subcategories: ISubcategorie[] = subcategoriesToCheck.filter(isPresent);
    if (subcategories.length > 0) {
      const subcategorieCollectionIdentifiers = subcategorieCollection.map(
        subcategorieItem => getSubcategorieIdentifier(subcategorieItem)!
      );
      const subcategoriesToAdd = subcategories.filter(subcategorieItem => {
        const subcategorieIdentifier = getSubcategorieIdentifier(subcategorieItem);
        if (subcategorieIdentifier == null || subcategorieCollectionIdentifiers.includes(subcategorieIdentifier)) {
          return false;
        }
        subcategorieCollectionIdentifiers.push(subcategorieIdentifier);
        return true;
      });
      return [...subcategoriesToAdd, ...subcategorieCollection];
    }
    return subcategorieCollection;
  }

  protected convertDateFromClient(subcategorie: ISubcategorie): ISubcategorie {
    return Object.assign({}, subcategorie, {
      createdAt: subcategorie.createdAt?.isValid() ? subcategorie.createdAt.toJSON() : undefined,
      updatedAt: subcategorie.updatedAt?.isValid() ? subcategorie.updatedAt.toJSON() : undefined,
      deletedAt: subcategorie.deletedAt?.isValid() ? subcategorie.deletedAt.toJSON() : undefined,
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
      res.body.forEach((subcategorie: ISubcategorie) => {
        subcategorie.createdAt = subcategorie.createdAt ? dayjs(subcategorie.createdAt) : undefined;
        subcategorie.updatedAt = subcategorie.updatedAt ? dayjs(subcategorie.updatedAt) : undefined;
        subcategorie.deletedAt = subcategorie.deletedAt ? dayjs(subcategorie.deletedAt) : undefined;
      });
    }
    return res;
  }
}
