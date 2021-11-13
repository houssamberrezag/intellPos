import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PersonTypes } from 'app/entities/enumerations/person-types.model';
import { IPerson, Person } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { Product } from 'app/entities/product/product.model';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { Sell } from '../sell.model';
declare let $: any;
@Component({
  selector: 'jhi-pos',
  templateUrl: './pos.component.html',
  styleUrls: ['./pos.component.scss'],
})
export class PosComponent implements OnInit {
  clients: IPerson[] = [];
  selectedProducts: Product[] = [];
  sells: Sell[] = [];

  clientForm: FormGroup = this.formBuilder.group({
    firstName: [null, [Validators.required, Validators.maxLength(191)]],
    lastName: [null, [Validators.required, Validators.maxLength(191)]],
    companyName: [null, [Validators.maxLength(191)]],
    email: [null, [Validators.maxLength(191)]],
    phone: [null, [Validators.required, Validators.maxLength(191)]],
    address: [null, [Validators.maxLength(191)]],
  });

  constructor(private formBuilder: FormBuilder, private personService: PersonService) {}

  ngOnInit(): void {
    this.hideSideBar();
    this.loadRelationshipsOptions();
  }

  hideSideBar(): void {
    $('body').addClass('closed-sidebar');
    $('#close-sidebar').addClass('hidden');
  }

  initClientForm(): void {
    this.clientForm.patchValue({
      firstName: '',
      lastName: '',
      companyName: '',
      email: '',
      phone: '',
      address: '',
    });
  }
  addClient(): void {
    const person = this.createFromForm();
    this.subscribeToSaveResponse(this.personService.create(person));
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .clients()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .subscribe((people: IPerson[]) => (this.clients = people));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res.body ?? {}),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(client: IPerson): void {
    //this.previousState();
    if (client.id) {
      this.clients.push(client);
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    //this.isSaving = false;
    console.log('save final');
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      firstName: this.clientForm.get(['firstName'])!.value,
      lastName: this.clientForm.get(['lastName'])!.value,
      companyName: this.clientForm.get(['companyName'])!.value,
      email: this.clientForm.get(['email'])!.value,
      phone: this.clientForm.get(['phone'])!.value,
      address: this.clientForm.get(['address'])!.value,
      personType: PersonTypes.CLIENT,
    };
  }
}
