import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReturnTransaction, ReturnTransaction } from '../return-transaction.model';
import { ReturnTransactionService } from '../service/return-transaction.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ISell } from 'app/entities/sell/sell.model';
import { SellService } from 'app/entities/sell/service/sell.service';

@Component({
  selector: 'jhi-return-transaction-update',
  templateUrl: './return-transaction-update.component.html',
})
export class ReturnTransactionUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPerson[] = [];
  sellsSharedCollection: ISell[] = [];

  editForm = this.fb.group({
    id: [],
    returnVat: [null, [Validators.required]],
    sellsReferenceNo: [null, [Validators.required, Validators.maxLength(191)]],
    returnUnits: [null, [Validators.required]],
    returnAmount: [null, [Validators.required]],
    returnedBy: [null, [Validators.required]],
    createdAt: [],
    updatedAt: [],
    deletedAt: [],
    person: [],
    sell: [],
  });

  constructor(
    protected returnTransactionService: ReturnTransactionService,
    protected personService: PersonService,
    protected sellService: SellService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnTransaction }) => {
      if (returnTransaction.id === undefined) {
        const today = dayjs().startOf('day');
        returnTransaction.createdAt = today;
        returnTransaction.updatedAt = today;
        returnTransaction.deletedAt = today;
      }

      this.updateForm(returnTransaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const returnTransaction = this.createFromForm();
    if (returnTransaction.id !== undefined) {
      this.subscribeToSaveResponse(this.returnTransactionService.update(returnTransaction));
    } else {
      this.subscribeToSaveResponse(this.returnTransactionService.create(returnTransaction));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackSellById(index: number, item: ISell): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReturnTransaction>>): void {
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

  protected updateForm(returnTransaction: IReturnTransaction): void {
    this.editForm.patchValue({
      id: returnTransaction.id,
      returnVat: returnTransaction.returnVat,
      sellsReferenceNo: returnTransaction.sellsReferenceNo,
      returnUnits: returnTransaction.returnUnits,
      returnAmount: returnTransaction.returnAmount,
      returnedBy: returnTransaction.returnedBy,
      createdAt: returnTransaction.createdAt ? returnTransaction.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: returnTransaction.updatedAt ? returnTransaction.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: returnTransaction.deletedAt ? returnTransaction.deletedAt.format(DATE_TIME_FORMAT) : null,
      person: returnTransaction.person,
      sell: returnTransaction.sell,
    });

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, returnTransaction.person);
    this.sellsSharedCollection = this.sellService.addSellToCollectionIfMissing(this.sellsSharedCollection, returnTransaction.sell);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.sellService
      .query()
      .pipe(map((res: HttpResponse<ISell[]>) => res.body ?? []))
      .pipe(map((sells: ISell[]) => this.sellService.addSellToCollectionIfMissing(sells, this.editForm.get('sell')!.value)))
      .subscribe((sells: ISell[]) => (this.sellsSharedCollection = sells));
  }

  protected createFromForm(): IReturnTransaction {
    return {
      ...new ReturnTransaction(),
      id: this.editForm.get(['id'])!.value,
      returnVat: this.editForm.get(['returnVat'])!.value,
      sellsReferenceNo: this.editForm.get(['sellsReferenceNo'])!.value,
      returnUnits: this.editForm.get(['returnUnits'])!.value,
      returnAmount: this.editForm.get(['returnAmount'])!.value,
      returnedBy: this.editForm.get(['returnedBy'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      deletedAt: this.editForm.get(['deletedAt'])!.value ? dayjs(this.editForm.get(['deletedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      person: this.editForm.get(['person'])!.value,
      sell: this.editForm.get(['sell'])!.value,
    };
  }
}
