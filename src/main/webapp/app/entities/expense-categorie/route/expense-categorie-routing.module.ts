import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpenseCategorieComponent } from '../list/expense-categorie.component';
import { ExpenseCategorieDetailComponent } from '../detail/expense-categorie-detail.component';
import { ExpenseCategorieUpdateComponent } from '../update/expense-categorie-update.component';
import { ExpenseCategorieRoutingResolveService } from './expense-categorie-routing-resolve.service';

const expenseCategorieRoute: Routes = [
  {
    path: '',
    component: ExpenseCategorieComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpenseCategorieDetailComponent,
    resolve: {
      expenseCategorie: ExpenseCategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpenseCategorieUpdateComponent,
    resolve: {
      expenseCategorie: ExpenseCategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpenseCategorieUpdateComponent,
    resolve: {
      expenseCategorie: ExpenseCategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expenseCategorieRoute)],
  exports: [RouterModule],
})
export class ExpenseCategorieRoutingModule {}
