import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaxeComponent } from '../list/taxe.component';
import { TaxeDetailComponent } from '../detail/taxe-detail.component';
import { TaxeUpdateComponent } from '../update/taxe-update.component';
import { TaxeRoutingResolveService } from './taxe-routing-resolve.service';

const taxeRoute: Routes = [
  {
    path: '',
    component: TaxeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaxeDetailComponent,
    resolve: {
      taxe: TaxeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaxeUpdateComponent,
    resolve: {
      taxe: TaxeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaxeUpdateComponent,
    resolve: {
      taxe: TaxeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(taxeRoute)],
  exports: [RouterModule],
})
export class TaxeRoutingModule {}
