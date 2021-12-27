import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SettingsRoutingModule } from './route/settings-routing.module';
import { SettingsUpdateComponent } from './update/settings-update.component';

@NgModule({
  imports: [SharedModule, SettingsRoutingModule],
  declarations: [SettingsUpdateComponent],
  entryComponents: [],
})
export class SettingsModule {}
