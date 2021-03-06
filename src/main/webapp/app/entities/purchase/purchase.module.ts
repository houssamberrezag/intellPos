import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PurchaseComponent } from './list/purchase.component';
import { PurchaseDetailComponent } from './detail/purchase-detail.component';
import { PurchaseUpdateComponent } from './update/purchase-update.component';
import { PurchaseDeleteDialogComponent } from './delete/purchase-delete-dialog.component';
import { PurchaseRoutingModule } from './route/purchase-routing.module';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { TodayPurchasesComponent } from './today-purchases/today-purchases.component';

@NgModule({
  imports: [SharedModule, PurchaseRoutingModule, MatDatepickerModule],
  declarations: [
    PurchaseComponent,
    PurchaseDetailComponent,
    PurchaseUpdateComponent,
    PurchaseDeleteDialogComponent,
    TodayPurchasesComponent,
  ],
  entryComponents: [PurchaseDeleteDialogComponent],
})
export class PurchaseModule {}
