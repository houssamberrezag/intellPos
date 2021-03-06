import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPurchase, Purchase } from '../purchase.model';
import { PurchaseService } from '../service/purchase.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';
import { AlertService } from 'app/core/util/alert.service';
import { DEFAULT_SUPPLIER } from 'app/shared/constants/constants';

@Component({
  selector: 'jhi-purchase-update',
  templateUrl: './purchase-update.component.html',
})
export class PurchaseUpdateComponent implements OnInit {
  isSaving = false;
  supplier: IPerson = {};
  purchases: IPurchase[] = [];
  peopleSharedCollection: IPerson[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    date: [],
    discount: [0],
    discountType: ['fixed'],
    //discountAmount: [],
    paymentMethod: ['CASH', [Validators.required, Validators.maxLength(50)]],
    paid: [0, [Validators.required]],
  });

  constructor(
    protected purchaseService: PurchaseService,
    protected personService: PersonService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private sweetAlertService: SweetAlertService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.loadRelationshipsOptions();
    this.newPurchase();
  }

  newPurchase(): void {
    this.purchases.push({
      unitCost: 0,
      quantity: 1,
    });
  }
  removePurchase(index: number): void {
    this.purchases.splice(index, 1);
  }

  onProductSelectedChange(purchase: IPurchase): void {
    purchase.unitCost = purchase.product?.costPrice ?? 0;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const { paid, paymentMethod } = this.editForm.value;
    if (this.calculeTotalNet() < 0) {
      this.sweetAlertService.create('R??duction invalide', 'Vous ne pouvez pas ins??rer une r??duction sup??rieure au total', 'error');
      //this.addErrorAlert("Vous ne pouvez pas ins??rer une r??duction sup??rieure au total");
      this.isSaving = false;
      return;
    }
    if (this.calculeTotalNet() < paid) {
      this.sweetAlertService.create('Montant pay?? invalide', 'le montant pay?? ne doit pas ??tre sup??rieur au total', 'error');
      //this.addErrorAlert("Le montant pay?? ne doit pas ??tre sup??rieur au total");
      this.isSaving = false;
      return;
    }
    this.purchases.forEach(purchase => {
      purchase.person = this.supplier;
    });
    console.log(this.purchases);

    this.subscribeToSaveResponse(this.purchaseService.create2(this.purchases, paid ?? 0, this.calculeDiscountAmount(), paymentMethod));
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  calculeTotal(): number {
    return this.purchases.map(p => (p.quantity ?? 0) * (p.unitCost ?? 0)).reduce((acc, value) => acc + value, 0);
  }

  calculeDiscountAmount(): number {
    const { discount, discountType } = this.editForm.value;
    const isFixedDiscount: boolean = discountType === 'fixed';
    const total: number = this.calculeTotal();
    return isFixedDiscount ? +discount : (total * discount) / 100;
  }

  calculeTotalNet(): number {
    return this.calculeTotal() - this.calculeDiscountAmount();
  }

  protected addErrorAlert(message?: string): void {
    this.alertService.addAlert({ type: 'danger', message });
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchase>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      error => this.onSaveError(error.error)
    );
  }

  protected onSaveSuccess(): void {
    this.productService.loadProductsAlert();
    this.previousState();
  }

  protected onSaveError(error: any): void {
    this.sweetAlertService.create(error.title, error.errorKey, 'error');
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  /*  protected updateForm(purchase: IPurchase): void {
    this.editForm.patchValue({
      id: purchase.id,
      referenceNo: purchase.referenceNo,
      quantity: purchase.quantity,
      subTotal: purchase.subTotal,
      productTax: purchase.productTax,
      date: purchase.date ? purchase.date.format(DATE_TIME_FORMAT) : null,
      createdAt: purchase.createdAt ? purchase.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: purchase.updatedAt ? purchase.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: purchase.deletedAt ? purchase.deletedAt.format(DATE_TIME_FORMAT) : null,
      person: purchase.person,
      product: purchase.product,
    }); 

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, purchase.person);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, purchase.product);
  }*/

  protected loadRelationshipsOptions(): void {
    this.personService
      .fournisseurs()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      //.pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => {
        this.peopleSharedCollection = people;
        this.supplier = this.peopleSharedCollection[0] ?? {};
        //this.supplier = DEFAULT_SUPPLIER;
      });

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      /*.pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )*/
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  /* protected createFromForm(): IPurchase {
    return {
      ...new Purchase(),
      id: this.editForm.get(['id'])!.value,
      referenceNo: this.editForm.get(['referenceNo'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      subTotal: this.editForm.get(['subTotal'])!.value,
      productTax: this.editForm.get(['productTax'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      deletedAt: this.editForm.get(['deletedAt'])!.value ? dayjs(this.editForm.get(['deletedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      person: this.editForm.get(['person'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  } */
}
