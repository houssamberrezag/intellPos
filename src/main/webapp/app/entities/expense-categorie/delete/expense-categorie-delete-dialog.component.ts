import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpenseCategorie } from '../expense-categorie.model';
import { ExpenseCategorieService } from '../service/expense-categorie.service';

@Component({
  templateUrl: './expense-categorie-delete-dialog.component.html',
})
export class ExpenseCategorieDeleteDialogComponent {
  expenseCategorie?: IExpenseCategorie;

  constructor(protected expenseCategorieService: ExpenseCategorieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseCategorieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
