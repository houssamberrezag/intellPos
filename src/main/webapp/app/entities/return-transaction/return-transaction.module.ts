import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReturnTransactionComponent } from './list/return-transaction.component';
import { ReturnTransactionDetailComponent } from './detail/return-transaction-detail.component';
import { ReturnTransactionUpdateComponent } from './update/return-transaction-update.component';
import { ReturnTransactionDeleteDialogComponent } from './delete/return-transaction-delete-dialog.component';
import { ReturnTransactionRoutingModule } from './route/return-transaction-routing.module';

@NgModule({
  imports: [SharedModule, ReturnTransactionRoutingModule],
  declarations: [
    ReturnTransactionComponent,
    ReturnTransactionDetailComponent,
    ReturnTransactionUpdateComponent,
    ReturnTransactionDeleteDialogComponent,
  ],
  entryComponents: [ReturnTransactionDeleteDialogComponent],
})
export class ReturnTransactionModule {}
