import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICashRegister, CashRegister } from '../cash-register.model';
import { CashRegisterService } from '../service/cash-register.service';

@Component({
  selector: 'jhi-cash-register-update',
  templateUrl: './cash-register-update.component.html',
})
export class CashRegisterUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cashInHands: [null, [Validators.required]],
    date: [null, [Validators.required]],
    createdAt: [],
    updatedAt: [],
  });

  constructor(protected cashRegisterService: CashRegisterService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashRegister }) => {
      if (cashRegister.id === undefined) {
        const today = dayjs().startOf('day');
        cashRegister.createdAt = today;
        cashRegister.updatedAt = today;
      }

      this.updateForm(cashRegister);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashRegister = this.createFromForm();
    if (cashRegister.id !== undefined) {
      this.subscribeToSaveResponse(this.cashRegisterService.update(cashRegister));
    } else {
      this.subscribeToSaveResponse(this.cashRegisterService.create(cashRegister));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashRegister>>): void {
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

  protected updateForm(cashRegister: ICashRegister): void {
    this.editForm.patchValue({
      id: cashRegister.id,
      cashInHands: cashRegister.cashInHands,
      date: cashRegister.date,
      createdAt: cashRegister.createdAt ? cashRegister.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: cashRegister.updatedAt ? cashRegister.updatedAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ICashRegister {
    return {
      ...new CashRegister(),
      id: this.editForm.get(['id'])!.value,
      cashInHands: this.editForm.get(['cashInHands'])!.value,
      date: this.editForm.get(['date'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
