import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DamageComponent } from './list/damage.component';
import { DamageDetailComponent } from './detail/damage-detail.component';
import { DamageUpdateComponent } from './update/damage-update.component';
import { DamageDeleteDialogComponent } from './delete/damage-delete-dialog.component';
import { DamageRoutingModule } from './route/damage-routing.module';

@NgModule({
  imports: [SharedModule, DamageRoutingModule],
  declarations: [DamageComponent, DamageDetailComponent, DamageUpdateComponent, DamageDeleteDialogComponent],
  entryComponents: [DamageDeleteDialogComponent],
})
export class DamageModule {}
