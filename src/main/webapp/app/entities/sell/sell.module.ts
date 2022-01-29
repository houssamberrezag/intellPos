import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SellComponent } from './list/sell.component';
import { SellDetailComponent } from './detail/sell-detail.component';
import { SellUpdateComponent } from './update/sell-update.component';
import { SellDeleteDialogComponent } from './delete/sell-delete-dialog.component';
import { SellRoutingModule } from './route/sell-routing.module';
import { ReturnComponent } from './return/return.component';
import { PosComponent } from './pos/pos.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { TodaySellsComponent } from './today-sells/today-sells.component';

@NgModule({
  imports: [SharedModule, SellRoutingModule],
  declarations: [
    SellComponent,
    SellDetailComponent,
    SellUpdateComponent,
    SellDeleteDialogComponent,
    ReturnComponent,
    PosComponent,
    InvoiceComponent,
    TodaySellsComponent,
  ],
  entryComponents: [SellDeleteDialogComponent],
})
export class SellModule {}
