import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CashRegisterComponent } from '../list/cash-register.component';
import { CashRegisterDetailComponent } from '../detail/cash-register-detail.component';
import { CashRegisterUpdateComponent } from '../update/cash-register-update.component';
import { CashRegisterRoutingResolveService } from './cash-register-routing-resolve.service';

const cashRegisterRoute: Routes = [
  {
    path: '',
    component: CashRegisterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CashRegisterDetailComponent,
    resolve: {
      cashRegister: CashRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CashRegisterUpdateComponent,
    resolve: {
      cashRegister: CashRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CashRegisterUpdateComponent,
    resolve: {
      cashRegister: CashRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cashRegisterRoute)],
  exports: [RouterModule],
})
export class CashRegisterRoutingModule {}
