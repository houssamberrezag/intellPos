import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubcategorie, Subcategorie } from '../subcategorie.model';
import { SubcategorieService } from '../service/subcategorie.service';

@Injectable({ providedIn: 'root' })
export class SubcategorieRoutingResolveService implements Resolve<ISubcategorie> {
  constructor(protected service: SubcategorieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubcategorie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subcategorie: HttpResponse<Subcategorie>) => {
          if (subcategorie.body) {
            return of(subcategorie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Subcategorie());
  }
}
