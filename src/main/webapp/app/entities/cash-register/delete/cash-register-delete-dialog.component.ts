import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashRegister } from '../cash-register.model';
import { CashRegisterService } from '../service/cash-register.service';

@Component({
  templateUrl: './cash-register-delete-dialog.component.html',
})
export class CashRegisterDeleteDialogComponent {
  cashRegister?: ICashRegister;

  constructor(protected cashRegisterService: CashRegisterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cashRegisterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
