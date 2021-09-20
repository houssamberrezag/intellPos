import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { PersonTypes } from 'app/entities/enumerations/person-types.model';

@Component({
  selector: 'jhi-update-client',
  templateUrl: './update-client.component.html',
  styleUrls: ['./update-client.component.scss'],
})
export class UpdateClientComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required, Validators.maxLength(191)]],
    lastName: [null, [Validators.maxLength(191)]],
    companyName: [null, [Validators.maxLength(191)]],
    email: [null, [Validators.maxLength(191)]],
    phone: [null, [Validators.required, Validators.maxLength(191)]],
    address: [null, [Validators.required, Validators.maxLength(191)]],
    proviousDue: [],
    accountNo: [null, [Validators.maxLength(191)]],
    createdAt: [],
    updatedAt: [],
    deletedAt: [],
  });

  constructor(protected personService: PersonService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      if (person.id === undefined) {
        const today = dayjs().startOf('day');
        person.createdAt = today;
        person.updatedAt = today;
        person.deletedAt = today;
      }

      this.updateForm(person);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    console.log(person);

    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      firstName: person.firstName,
      lastName: person.lastName,
      companyName: person.companyName,
      email: person.email,
      phone: person.phone,
      address: person.address,
      proviousDue: person.proviousDue,
      accountNo: person.accountNo,
      createdAt: person.createdAt ? person.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: person.updatedAt ? person.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: person.deletedAt ? person.deletedAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      address: this.editForm.get(['address'])!.value,

      proviousDue: this.editForm.get(['proviousDue'])!.value,
      accountNo: this.editForm.get(['accountNo'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      deletedAt: this.editForm.get(['deletedAt'])!.value ? dayjs(this.editForm.get(['deletedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      personType: PersonTypes.CLIENT,
    };
  }
}
