import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaxe, Taxe } from '../taxe.model';
import { TaxeService } from '../service/taxe.service';

@Injectable({ providedIn: 'root' })
export class TaxeRoutingResolveService implements Resolve<ITaxe> {
  constructor(protected service: TaxeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaxe> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((taxe: HttpResponse<Taxe>) => {
          if (taxe.body) {
            return of(taxe.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Taxe());
  }
}
