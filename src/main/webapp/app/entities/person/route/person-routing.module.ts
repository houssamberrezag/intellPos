import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonComponent } from '../list/person.component';
import { PersonDetailComponent } from '../detail/person-detail.component';
import { PersonUpdateComponent } from '../update/person-update.component';
import { PersonRoutingResolveService } from './person-routing-resolve.service';
import { ListClientsComponent } from '../list-clients/list-clients.component';
import { UpdateClientComponent } from '../update-client/update-client.component';
import { ListFounisseursComponent } from '../founisseur/list-founisseurs/list-founisseurs.component';
import { UpdateFournisseurComponent } from '../founisseur/update-fournisseur/update-fournisseur.component';

const personRoute: Routes = [
  /*   {
    path: '',
    component: PersonComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonDetailComponent,
    resolve: {
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonUpdateComponent,
    resolve: {
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonUpdateComponent,
    resolve: {
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  }, */
  {
    path: 'client',
    component: ListClientsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'client/new',
    component: UpdateClientComponent,
    resolve: {
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'client/:id/edit',
    component: UpdateClientComponent,
    resolve: {
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'fournisseur',
    component: ListFounisseursComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'fournisseur/new',
    component: UpdateFournisseurComponent,
    resolve: {
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'fournisseur/:id/edit',
    component: UpdateFournisseurComponent,
    resolve: {
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personRoute)],
  exports: [RouterModule],
})
export class PersonRoutingModule {}
