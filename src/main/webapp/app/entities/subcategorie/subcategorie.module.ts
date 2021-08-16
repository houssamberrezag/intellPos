import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SubcategorieComponent } from './list/subcategorie.component';
import { SubcategorieDetailComponent } from './detail/subcategorie-detail.component';
import { SubcategorieUpdateComponent } from './update/subcategorie-update.component';
import { SubcategorieDeleteDialogComponent } from './delete/subcategorie-delete-dialog.component';
import { SubcategorieRoutingModule } from './route/subcategorie-routing.module';

@NgModule({
  imports: [SharedModule, SubcategorieRoutingModule],
  declarations: [SubcategorieComponent, SubcategorieDetailComponent, SubcategorieUpdateComponent, SubcategorieDeleteDialogComponent],
  entryComponents: [SubcategorieDeleteDialogComponent],
})
export class SubcategorieModule {}
