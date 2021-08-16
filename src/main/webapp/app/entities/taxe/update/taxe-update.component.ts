import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITaxe, Taxe } from '../taxe.model';
import { TaxeService } from '../service/taxe.service';

@Component({
  selector: 'jhi-taxe-update',
  templateUrl: './taxe-update.component.html',
})
export class TaxeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    rate: [null, [Validators.required]],
    type: [null, [Validators.required]],
    createdAt: [],
    updatedAt: [],
  });

  constructor(protected taxeService: TaxeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxe }) => {
      if (taxe.id === undefined) {
        const today = dayjs().startOf('day');
        taxe.createdAt = today;
        taxe.updatedAt = today;
      }

      this.updateForm(taxe);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taxe = this.createFromForm();
    if (taxe.id !== undefined) {
      this.subscribeToSaveResponse(this.taxeService.update(taxe));
    } else {
      this.subscribeToSaveResponse(this.taxeService.create(taxe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaxe>>): void {
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

  protected updateForm(taxe: ITaxe): void {
    this.editForm.patchValue({
      id: taxe.id,
      name: taxe.name,
      rate: taxe.rate,
      type: taxe.type,
      createdAt: taxe.createdAt ? taxe.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: taxe.updatedAt ? taxe.updatedAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ITaxe {
    return {
      ...new Taxe(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      rate: this.editForm.get(['rate'])!.value,
      type: this.editForm.get(['type'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
