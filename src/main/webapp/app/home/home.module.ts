import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { SellPurchaseItemComponent } from './dashboardItems/sell-purchase-item/sell-purchase-item.component';
import { NgChartsModule } from 'ng2-charts';
import { StockItemComponent } from './dashboardItems/stock-item/stock-item.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]), NgChartsModule],
  declarations: [HomeComponent, SellPurchaseItemComponent, StockItemComponent],
})
export class HomeModule {}
