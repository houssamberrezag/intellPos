import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReturnTransactionComponent } from '../list/return-transaction.component';
import { ReturnTransactionDetailComponent } from '../detail/return-transaction-detail.component';
import { ReturnTransactionUpdateComponent } from '../update/return-transaction-update.component';
import { ReturnTransactionRoutingResolveService } from './return-transaction-routing-resolve.service';

const returnTransactionRoute: Routes = [
  {
    path: '',
    component: ReturnTransactionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReturnTransactionDetailComponent,
    resolve: {
      returnTransaction: ReturnTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReturnTransactionUpdateComponent,
    resolve: {
      returnTransaction: ReturnTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReturnTransactionUpdateComponent,
    resolve: {
      returnTransaction: ReturnTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(returnTransactionRoute)],
  exports: [RouterModule],
})
export class ReturnTransactionRoutingModule {}
