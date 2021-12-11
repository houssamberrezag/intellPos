import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { ITransaction, Transaction } from 'app/entities/transaction/transaction.model';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class InvoiceRoutingResolveService implements Resolve<ITransaction> {
  constructor(protected service: TransactionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransaction> | Observable<never> {
    const ref = route.params['ref'];
    if (ref) {
      return this.service.findByRef(ref).pipe(
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
