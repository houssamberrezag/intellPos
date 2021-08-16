import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReturnTransaction } from '../return-transaction.model';
import { ReturnTransactionService } from '../service/return-transaction.service';

@Component({
  templateUrl: './return-transaction-delete-dialog.component.html',
})
export class ReturnTransactionDeleteDialogComponent {
  returnTransaction?: IReturnTransaction;

  constructor(protected returnTransactionService: ReturnTransactionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.returnTransactionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
