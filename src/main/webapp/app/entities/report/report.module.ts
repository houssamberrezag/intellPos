import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportComponent } from './report/report.component';
import { ReportRoutingModule } from './route/report-routing.module';
import { ProductReportComponent } from './product/product-report.component';
import { PurchaseReportComponent } from './purchase/purchase-report.component';
import { SellReportComponent } from './sell/sell-report.component';
import { StockReportComponent } from './stock/stock-report.component';

@NgModule({
  imports: [SharedModule, ReportRoutingModule],
  declarations: [ReportComponent, ProductReportComponent, PurchaseReportComponent, SellReportComponent, StockReportComponent],
})
export class ReportModule {}
