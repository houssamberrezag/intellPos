import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettingsUpdateComponent } from '../update/settings-update.component';
import { SettingsRoutingResolveService } from './settings-routing-resolve.service';

const settingsRoute: Routes = [
  {
    path: '',
    component: SettingsUpdateComponent,

    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(settingsRoute)],
  exports: [RouterModule],
})
export class SettingsRoutingModule {}
