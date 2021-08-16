import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExpenseCategorie, ExpenseCategorie } from '../expense-categorie.model';
import { ExpenseCategorieService } from '../service/expense-categorie.service';

@Component({
  selector: 'jhi-expense-categorie-update',
  templateUrl: './expense-categorie-update.component.html',
})
export class ExpenseCategorieUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(191)]],
    createdAt: [],
    updatedAt: [],
  });

  constructor(
    protected expenseCategorieService: ExpenseCategorieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseCategorie }) => {
      if (expenseCategorie.id === undefined) {
        const today = dayjs().startOf('day');
        expenseCategorie.createdAt = today;
        expenseCategorie.updatedAt = today;
      }

      this.updateForm(expenseCategorie);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseCategorie = this.createFromForm();
    if (expenseCategorie.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseCategorieService.update(expenseCategorie));
    } else {
      this.subscribeToSaveResponse(this.expenseCategorieService.create(expenseCategorie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseCategorie>>): void {
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

  protected updateForm(expenseCategorie: IExpenseCategorie): void {
    this.editForm.patchValue({
      id: expenseCategorie.id,
      name: expenseCategorie.name,
      createdAt: expenseCategorie.createdAt ? expenseCategorie.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: expenseCategorie.updatedAt ? expenseCategorie.updatedAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IExpenseCategorie {
    return {
      ...new ExpenseCategorie(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
