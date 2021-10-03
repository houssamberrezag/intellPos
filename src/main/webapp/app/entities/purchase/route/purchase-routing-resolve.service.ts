import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransaction, Transaction } from 'app/entities/transaction/transaction.model';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';

@Injectable({ providedIn: 'root' })
export class PurchaseRoutingResolveService implements Resolve<ITransaction> {
  constructor(protected service: TransactionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transaction: HttpResponse<ITransaction>) => {
          if (transaction.body) {
            return of(transaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Transaction());
  }
}
