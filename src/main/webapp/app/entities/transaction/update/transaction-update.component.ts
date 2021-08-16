import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITransaction, Transaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    referenceNo: [null, [Validators.required, Validators.maxLength(191)]],
    transactionType: [null, [Validators.required]],
    totalCostPrice: [],
    discount: [null, [Validators.required]],
    total: [null, [Validators.required]],
    invoiceTax: [],
    totalTax: [],
    laborCost: [null, [Validators.required]],
    netTotal: [],
    paid: [null, [Validators.required]],
    changeAmount: [],
    returnInvoice: [null, [Validators.maxLength(191)]],
    returnBalance: [],
    pos: [null, [Validators.required]],
    date: [],
    createdAt: [],
    updatedAt: [],
    deletedAt: [],
    person: [],
  });

  constructor(
    protected transactionService: TransactionService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      if (transaction.id === undefined) {
        const today = dayjs().startOf('day');
        transaction.date = today;
        transaction.createdAt = today;
        transaction.updatedAt = today;
        transaction.deletedAt = today;
      }

      this.updateForm(transaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      referenceNo: transaction.referenceNo,
      transactionType: transaction.transactionType,
      totalCostPrice: transaction.totalCostPrice,
      discount: transaction.discount,
      total: transaction.total,
      invoiceTax: transaction.invoiceTax,
      totalTax: transaction.totalTax,
      laborCost: transaction.laborCost,
      netTotal: transaction.netTotal,
      paid: transaction.paid,
      changeAmount: transaction.changeAmount,
      returnInvoice: transaction.returnInvoice,
      returnBalance: transaction.returnBalance,
      pos: transaction.pos,
      date: transaction.date ? transaction.date.format(DATE_TIME_FORMAT) : null,
      createdAt: transaction.createdAt ? transaction.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: transaction.updatedAt ? transaction.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: transaction.deletedAt ? transaction.deletedAt.format(DATE_TIME_FORMAT) : null,
      person: transaction.person,
    });

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, transaction.person);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      referenceNo: this.editForm.get(['referenceNo'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      totalCostPrice: this.editForm.get(['totalCostPrice'])!.value,
      discount: this.editForm.get(['discount'])!.value,
      total: this.editForm.get(['total'])!.value,
      invoiceTax: this.editForm.get(['invoiceTax'])!.value,
      totalTax: this.editForm.get(['totalTax'])!.value,
      laborCost: this.editForm.get(['laborCost'])!.value,
      netTotal: this.editForm.get(['netTotal'])!.value,
      paid: this.editForm.get(['paid'])!.value,
      changeAmount: this.editForm.get(['changeAmount'])!.value,
      returnInvoice: this.editForm.get(['returnInvoice'])!.value,
      returnBalance: this.editForm.get(['returnBalance'])!.value,
      pos: this.editForm.get(['pos'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      deletedAt: this.editForm.get(['deletedAt'])!.value ? dayjs(this.editForm.get(['deletedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
