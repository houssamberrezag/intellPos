import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TaxeComponent } from './list/taxe.component';
import { TaxeDetailComponent } from './detail/taxe-detail.component';
import { TaxeUpdateComponent } from './update/taxe-update.component';
import { TaxeDeleteDialogComponent } from './delete/taxe-delete-dialog.component';
import { TaxeRoutingModule } from './route/taxe-routing.module';

@NgModule({
  imports: [SharedModule, TaxeRoutingModule],
  declarations: [TaxeComponent, TaxeDetailComponent, TaxeUpdateComponent, TaxeDeleteDialogComponent],
  entryComponents: [TaxeDeleteDialogComponent],
})
export class TaxeModule {}
