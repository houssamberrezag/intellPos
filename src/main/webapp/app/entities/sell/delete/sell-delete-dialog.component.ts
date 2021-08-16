import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISell } from '../sell.model';
import { SellService } from '../service/sell.service';

@Component({
  templateUrl: './sell-delete-dialog.component.html',
})
export class SellDeleteDialogComponent {
  sell?: ISell;

  constructor(protected sellService: SellService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sellService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
