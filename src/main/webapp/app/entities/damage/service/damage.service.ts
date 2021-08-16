import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDamage, getDamageIdentifier } from '../damage.model';

export type EntityResponseType = HttpResponse<IDamage>;
export type EntityArrayResponseType = HttpResponse<IDamage[]>;

@Injectable({ providedIn: 'root' })
export class DamageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/damages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(damage: IDamage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(damage);
    return this.http
      .post<IDamage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(damage: IDamage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(damage);
    return this.http
      .put<IDamage>(`${this.resourceUrl}/${getDamageIdentifier(damage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(damage: IDamage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(damage);
    return this.http
      .patch<IDamage>(`${this.resourceUrl}/${getDamageIdentifier(damage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDamage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDamage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDamageToCollectionIfMissing(damageCollection: IDamage[], ...damagesToCheck: (IDamage | null | undefined)[]): IDamage[] {
    const damages: IDamage[] = damagesToCheck.filter(isPresent);
    if (damages.length > 0) {
      const damageCollectionIdentifiers = damageCollection.map(damageItem => getDamageIdentifier(damageItem)!);
      const damagesToAdd = damages.filter(damageItem => {
        const damageIdentifier = getDamageIdentifier(damageItem);
        if (damageIdentifier == null || damageCollectionIdentifiers.includes(damageIdentifier)) {
          return false;
        }
        damageCollectionIdentifiers.push(damageIdentifier);
        return true;
      });
      return [...damagesToAdd, ...damageCollection];
    }
    return damageCollection;
  }

  protected convertDateFromClient(damage: IDamage): IDamage {
    return Object.assign({}, damage, {
      date: damage.date?.isValid() ? damage.date.toJSON() : undefined,
      createdAt: damage.createdAt?.isValid() ? damage.createdAt.toJSON() : undefined,
      updatedAt: damage.updatedAt?.isValid() ? damage.updatedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((damage: IDamage) => {
        damage.date = damage.date ? dayjs(damage.date) : undefined;
        damage.createdAt = damage.createdAt ? dayjs(damage.createdAt) : undefined;
        damage.updatedAt = damage.updatedAt ? dayjs(damage.updatedAt) : undefined;
      });
    }
    return res;
  }
}
