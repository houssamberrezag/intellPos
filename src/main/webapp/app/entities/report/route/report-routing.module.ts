import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductReportComponent } from '../product/product-report.component';
import { ReportComponent } from '../report/report.component';

const categorieRoute: Routes = [
  {
    path: '',
    component: ReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'products',
    component: ProductReportComponent,
    /*resolve: {
      categorie: CategorieRoutingResolveService,
    },*/
    canActivate: [UserRouteAccessService],
  },
  /*{
    path: 'new',
    component: CategorieUpdateComponent,
    resolve: {
      categorie: CategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategorieUpdateComponent,
    resolve: {
      categorie: CategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },*/
];

@NgModule({
  imports: [RouterModule.forChild(categorieRoute)],
  exports: [RouterModule],
})
export class ReportRoutingModule {}
