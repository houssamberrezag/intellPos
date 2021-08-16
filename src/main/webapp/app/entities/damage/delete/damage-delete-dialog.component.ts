import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDamage } from '../damage.model';
import { DamageService } from '../service/damage.service';

@Component({
  templateUrl: './damage-delete-dialog.component.html',
})
export class DamageDeleteDialogComponent {
  damage?: IDamage;

  constructor(protected damageService: DamageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.damageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
