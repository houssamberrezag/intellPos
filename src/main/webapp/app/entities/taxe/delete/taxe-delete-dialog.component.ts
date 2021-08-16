import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaxe } from '../taxe.model';
import { TaxeService } from '../service/taxe.service';

@Component({
  templateUrl: './taxe-delete-dialog.component.html',
})
export class TaxeDeleteDialogComponent {
  taxe?: ITaxe;

  constructor(protected taxeService: TaxeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taxeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
