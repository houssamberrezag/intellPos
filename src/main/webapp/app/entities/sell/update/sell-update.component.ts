import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISell, Sell } from '../sell.model';
import { SellService } from '../service/sell.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-sell-update',
  templateUrl: './sell-update.component.html',
})
export class SellUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPerson[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    referenceNo: [null, [Validators.required, Validators.maxLength(191)]],
    quantity: [null, [Validators.required]],
    unitCostPrice: [],
    subTotal: [null, [Validators.required]],
    productTax: [],
    date: [],
    createdAt: [],
    updatedAt: [],
    deletedAt: [],
    person: [],
    product: [],
  });

  constructor(
    protected sellService: SellService,
    protected personService: PersonService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sell }) => {
      if (sell.id === undefined) {
        const today = dayjs().startOf('day');
        sell.date = today;
        sell.createdAt = today;
        sell.updatedAt = today;
        sell.deletedAt = today;
      }

      this.updateForm(sell);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sell = this.createFromForm();
    if (sell.id !== undefined) {
      this.subscribeToSaveResponse(this.sellService.update(sell));
    } else {
      this.subscribeToSaveResponse(this.sellService.create(sell));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISell>>): void {
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

  protected updateForm(sell: ISell): void {
    this.editForm.patchValue({
      id: sell.id,
      referenceNo: sell.referenceNo,
      quantity: sell.quantity,
      unitCostPrice: sell.unitCostPrice,
      subTotal: sell.subTotal,
      productTax: sell.productTax,
      date: sell.date ? sell.date.format(DATE_TIME_FORMAT) : null,
      createdAt: sell.createdAt ? sell.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: sell.updatedAt ? sell.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: sell.deletedAt ? sell.deletedAt.format(DATE_TIME_FORMAT) : null,
      person: sell.person,
      product: sell.product,
    });

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, sell.person);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, sell.product);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): ISell {
    return {
      ...new Sell(),
      id: this.editForm.get(['id'])!.value,
      referenceNo: this.editForm.get(['referenceNo'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      unitCostPrice: this.editForm.get(['unitCostPrice'])!.value,
      subTotal: this.editForm.get(['subTotal'])!.value,
      productTax: this.editForm.get(['productTax'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      deletedAt: this.editForm.get(['deletedAt'])!.value ? dayjs(this.editForm.get(['deletedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      person: this.editForm.get(['person'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
