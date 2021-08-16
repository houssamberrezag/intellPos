import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CashRegisterComponent } from './list/cash-register.component';
import { CashRegisterDetailComponent } from './detail/cash-register-detail.component';
import { CashRegisterUpdateComponent } from './update/cash-register-update.component';
import { CashRegisterDeleteDialogComponent } from './delete/cash-register-delete-dialog.component';
import { CashRegisterRoutingModule } from './route/cash-register-routing.module';

@NgModule({
  imports: [SharedModule, CashRegisterRoutingModule],
  declarations: [CashRegisterComponent, CashRegisterDetailComponent, CashRegisterUpdateComponent, CashRegisterDeleteDialogComponent],
  entryComponents: [CashRegisterDeleteDialogComponent],
})
export class CashRegisterModule {}
