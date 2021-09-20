import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubcategorieComponent } from '../list/subcategorie.component';
import { SubcategorieDetailComponent } from '../detail/subcategorie-detail.component';
import { SubcategorieUpdateComponent } from '../update/subcategorie-update.component';
import { SubcategorieRoutingResolveService } from './subcategorie-routing-resolve.service';
import { ProductComponent } from '../product/product.component';

const subcategorieRoute: Routes = [
  {
    path: '',
    component: SubcategorieComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubcategorieDetailComponent,
    resolve: {
      subcategorie: SubcategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubcategorieUpdateComponent,
    resolve: {
      subcategorie: SubcategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubcategorieUpdateComponent,
    resolve: {
      subcategorie: SubcategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/product',
    component: ProductComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      subcategorie: SubcategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subcategorieRoute)],
  exports: [RouterModule],
})
export class SubcategorieRoutingModule {}
