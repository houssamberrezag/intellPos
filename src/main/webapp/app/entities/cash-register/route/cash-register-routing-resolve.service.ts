import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICashRegister, CashRegister } from '../cash-register.model';
import { CashRegisterService } from '../service/cash-register.service';

@Injectable({ providedIn: 'root' })
export class CashRegisterRoutingResolveService implements Resolve<ICashRegister> {
  constructor(protected service: CashRegisterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashRegister> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cashRegister: HttpResponse<CashRegister>) => {
          if (cashRegister.body) {
            return of(cashRegister.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashRegister());
  }
}
