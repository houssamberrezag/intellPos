import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SellComponent } from './list/sell.component';
import { SellDetailComponent } from './detail/sell-detail.component';
import { SellUpdateComponent } from './update/sell-update.component';
import { SellDeleteDialogComponent } from './delete/sell-delete-dialog.component';
import { SellRoutingModule } from './route/sell-routing.module';

@NgModule({
  imports: [SharedModule, SellRoutingModule],
  declarations: [SellComponent, SellDetailComponent, SellUpdateComponent, SellDeleteDialogComponent],
  entryComponents: [SellDeleteDialogComponent],
})
export class SellModule {}
