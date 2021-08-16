import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISell, Sell } from '../sell.model';
import { SellService } from '../service/sell.service';

@Injectable({ providedIn: 'root' })
export class SellRoutingResolveService implements Resolve<ISell> {
  constructor(protected service: SellService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISell> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sell: HttpResponse<Sell>) => {
          if (sell.body) {
            return of(sell.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sell());
  }
}
