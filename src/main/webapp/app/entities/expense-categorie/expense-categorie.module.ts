import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExpenseCategorieComponent } from './list/expense-categorie.component';
import { ExpenseCategorieDetailComponent } from './detail/expense-categorie-detail.component';
import { ExpenseCategorieUpdateComponent } from './update/expense-categorie-update.component';
import { ExpenseCategorieDeleteDialogComponent } from './delete/expense-categorie-delete-dialog.component';
import { ExpenseCategorieRoutingModule } from './route/expense-categorie-routing.module';

@NgModule({
  imports: [SharedModule, ExpenseCategorieRoutingModule],
  declarations: [
    ExpenseCategorieComponent,
    ExpenseCategorieDetailComponent,
    ExpenseCategorieUpdateComponent,
    ExpenseCategorieDeleteDialogComponent,
  ],
  entryComponents: [ExpenseCategorieDeleteDialogComponent],
})
export class ExpenseCategorieModule {}
