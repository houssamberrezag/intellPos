import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReturnTransaction, ReturnTransaction } from '../return-transaction.model';
import { ReturnTransactionService } from '../service/return-transaction.service';

@Injectable({ providedIn: 'root' })
export class ReturnTransactionRoutingResolveService implements Resolve<IReturnTransaction> {
  constructor(protected service: ReturnTransactionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReturnTransaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((returnTransaction: HttpResponse<ReturnTransaction>) => {
          if (returnTransaction.body) {
            return of(returnTransaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReturnTransaction());
  }
}
