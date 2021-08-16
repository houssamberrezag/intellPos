import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SellComponent } from '../list/sell.component';
import { SellDetailComponent } from '../detail/sell-detail.component';
import { SellUpdateComponent } from '../update/sell-update.component';
import { SellRoutingResolveService } from './sell-routing-resolve.service';

const sellRoute: Routes = [
  {
    path: '',
    component: SellComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SellDetailComponent,
    resolve: {
      sell: SellRoutingResolveService,
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
