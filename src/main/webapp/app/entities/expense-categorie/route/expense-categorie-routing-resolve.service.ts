import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpenseCategorie, ExpenseCategorie } from '../expense-categorie.model';
import { ExpenseCategorieService } from '../service/expense-categorie.service';

@Injectable({ providedIn: 'root' })
export class ExpenseCategorieRoutingResolveService implements Resolve<IExpenseCategorie> {
  constructor(protected service: ExpenseCategorieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpenseCategorie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((expenseCategorie: HttpResponse<ExpenseCategorie>) => {
          if (expenseCategorie.body) {
            return of(expenseCategorie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExpenseCategorie());
  }
}
