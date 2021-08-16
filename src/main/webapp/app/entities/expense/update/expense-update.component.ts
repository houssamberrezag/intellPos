import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExpense, Expense } from '../expense.model';
import { ExpenseService } from '../service/expense.service';
import { IExpenseCategorie } from 'app/entities/expense-categorie/expense-categorie.model';
import { ExpenseCategorieService } from 'app/entities/expense-categorie/service/expense-categorie.service';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;

  expenseCategoriesSharedCollection: IExpenseCategorie[] = [];

  editForm = this.fb.group({
    id: [],
    purpose: [null, [Validators.maxLength(191)]],
    amount: [null, [Validators.required]],
    createdAt: [],
    updatedAt: [],
    deletedAt: [],
    expenseCategorie: [],
  });

  constructor(
    protected expenseService: ExpenseService,
    protected expenseCategorieService: ExpenseCategorieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      if (expense.id === undefined) {
        const today = dayjs().startOf('day');
        expense.createdAt = today;
        expense.updatedAt = today;
        expense.deletedAt = today;
      }

      this.updateForm(expense);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.createFromForm();
    if (expense.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  trackExpenseCategorieById(index: number, item: IExpenseCategorie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
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

  protected updateForm(expense: IExpense): void {
    this.editForm.patchValue({
      id: expense.id,
      purpose: expense.purpose,
      amount: expense.amount,
      createdAt: expense.createdAt ? expense.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: expense.updatedAt ? expense.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: expense.deletedAt ? expense.deletedAt.format(DATE_TIME_FORMAT) : null,
      expenseCategorie: expense.expenseCategorie,
    });

    this.expenseCategoriesSharedCollection = this.expenseCategorieService.addExpenseCategorieToCollectionIfMissing(
      this.expenseCategoriesSharedCollection,
      expense.expenseCategorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.expenseCategorieService
      .query()
      .pipe(map((res: HttpResponse<IExpenseCategorie[]>) => res.body ?? []))
      .pipe(
        map((expenseCategories: IExpenseCategorie[]) =>
          this.expenseCategorieService.addExpenseCategorieToCollectionIfMissing(
            expenseCategories,
            this.editForm.get('expenseCategorie')!.value
          )
        )
      )
      .subscribe((expenseCategories: IExpenseCategorie[]) => (this.expenseCategoriesSharedCollection = expenseCategories));
  }

  protected createFromForm(): IExpense {
    return {
      ...new Expense(),
      id: this.editForm.get(['id'])!.value,
      purpose: this.editForm.get(['purpose'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      deletedAt: this.editForm.get(['deletedAt'])!.value ? dayjs(this.editForm.get(['deletedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      expenseCategorie: this.editForm.get(['expenseCategorie'])!.value,
    };
  }
}
