import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SellComponent } from '../list/sell.component';
import { SellDetailComponent } from '../detail/sell-detail.component';
import { SellUpdateComponent } from '../update/sell-update.component';
import { SellRoutingResolveService } from './sell-routing-resolve.service';
import { TransactionRoutingResolveService } from 'app/entities/transaction/route/transaction-routing-resolve.service';
import { ReturnComponent } from '../return/return.component';
import { PosComponent } from '../pos/pos.component';
import { InvoiceComponent } from '../invoice/invoice.component';
import { InvoiceRoutingResolveService } from './invoice-routing-resolve.service';

const sellRoute: Routes = [
  {
    path: '',
    component: SellComponent,
    data: {
      defaultSort: 'id,desc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SellDetailComponent,
    resolve: {
      transaction: TransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/return',
    component: ReturnComponent,
    resolve: {
      transaction: TransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SellUpdateComponent,
    resolve: {
      sell: SellRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'pos',
    component: PosComponent,
    resolve: {
      sell: SellRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'invoice/:ref',
    component: InvoiceComponent,
    resolve: {
      transaction: InvoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SellUpdateComponent,
    resolve: {
      sell: SellRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sellRoute)],
  exports: [RouterModule],
})
export class SellRoutingModule {}
