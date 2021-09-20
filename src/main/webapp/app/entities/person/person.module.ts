import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonComponent } from './list/person.component';
import { PersonDetailComponent } from './detail/person-detail.component';
import { PersonUpdateComponent } from './update/person-update.component';
import { PersonDeleteDialogComponent } from './delete/person-delete-dialog.component';
import { PersonRoutingModule } from './route/person-routing.module';
import { ListClientsComponent } from './list-clients/list-clients.component';
import { DetailClientComponent } from './detail-client/detail-client.component';
import { UpdateClientComponent } from './update-client/update-client.component';
import { ListFounisseursComponent } from './founisseur/list-founisseurs/list-founisseurs.component';
import { UpdateFournisseurComponent } from './founisseur/update-fournisseur/update-fournisseur.component';

@NgModule({
  imports: [SharedModule, PersonRoutingModule],
  declarations: [
    PersonComponent,
    PersonDetailComponent,
    PersonUpdateComponent,
    PersonDeleteDialogComponent,
    ListClientsComponent,
    DetailClientComponent,
    UpdateClientComponent,
    ListFounisseursComponent,
    UpdateFournisseurComponent,
  ],
  entryComponents: [PersonDeleteDialogComponent],
})
export class PersonModule {}
