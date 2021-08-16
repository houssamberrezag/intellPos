import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubcategorie } from '../subcategorie.model';
import { SubcategorieService } from '../service/subcategorie.service';

@Component({
  templateUrl: './subcategorie-delete-dialog.component.html',
})
export class SubcategorieDeleteDialogComponent {
  subcategorie?: ISubcategorie;

  constructor(protected subcategorieService: SubcategorieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subcategorieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
