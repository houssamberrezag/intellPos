import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDamage, Damage } from '../damage.model';
import { DamageService } from '../service/damage.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-damage-update',
  templateUrl: './damage-update.component.html',
})
export class DamageUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required]],
    date: [],
    note: [null, [Validators.maxLength(191)]],
    createdAt: [],
    updatedAt: [],
    product: [],
  });

  constructor(
    protected damageService: DamageService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ damage }) => {
      if (damage.id === undefined) {
        const today = dayjs().startOf('day');
        damage.date = today;
        damage.createdAt = today;
        damage.updatedAt = today;
      }

      this.updateForm(damage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const damage = this.createFromForm();
    if (damage.id !== undefined) {
      this.subscribeToSaveResponse(this.damageService.update(damage));
    } else {
      this.subscribeToSaveResponse(this.damageService.create(damage));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDamage>>): void {
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

  protected updateForm(damage: IDamage): void {
    this.editForm.patchValue({
      id: damage.id,
      quantity: damage.quantity,
      date: damage.date ? damage.date.format(DATE_TIME_FORMAT) : null,
      note: damage.note,
      createdAt: damage.createdAt ? damage.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: damage.updatedAt ? damage.updatedAt.format(DATE_TIME_FORMAT) : null,
      product: damage.product,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, damage.product);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): IDamage {
    return {
      ...new Damage(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      note: this.editForm.get(['note'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
