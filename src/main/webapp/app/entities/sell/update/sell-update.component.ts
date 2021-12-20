import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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
import { SweetAlertService } from 'app/core/util/sweet-alert.service';
import { AlertService } from 'app/core/util/alert.service';
import { DEFAULT_CLIENT } from 'app/shared/constants/constants';

@Component({
  selector: 'jhi-sell-update',
  templateUrl: './sell-update.component.html',
})
export class SellUpdateComponent implements OnInit {
  isSaving = false;
  client: IPerson = {};
  sells: ISell[] = [];
  peopleSharedCollection: IPerson[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    date: [new Date(), Validators.required],
    discount: [0],
    shippingCost: [0],
    discountType: ['fixed'],
    //discountAmount: [],
    paymentMethod: ['CASH', [Validators.required, Validators.maxLength(50)]],
    paid: [0, [Validators.required]],
  });
  constructor(
    protected sellService: SellService,
    protected personService: PersonService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private sweetAlertService: SweetAlertService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadRelationshipsOptions();
    this.newSell();
  }

  newSell(): void {
    this.sells.push({
      unitPrice: 0,
      quantity: 1,
    });
  }
  removeSell(index: number): void {
    this.sells.splice(index, 1);
  }

  onProductSelectedChange(sell: ISell): void {
    sell.unitPrice = sell.product?.minimumRetailPrice ?? 0;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    if (this.saveIsValid()) {
      this.isSaving = true;
      const { paid, paymentMethod, shippingCost } = this.editForm.value;
      if (this.calculeTotalNet() < 0) {
        this.sweetAlertService.create('Réduction invalide', 'Vous ne pouvez pas insérer une réduction supérieure au total', 'error');
        //this.addErrorAlert("Vous ne pouvez pas insérer une réduction supérieure au total");
        this.isSaving = false;
        return;
      }
      if (this.calculeTotalNet() < paid) {
        this.sweetAlertService.create('Montant payé invalide', 'le montant payé ne doit pas être supérieur au total', 'error');
        //this.addErrorAlert("Le montant payé ne doit pas être supérieur au total");
        this.isSaving = false;
        return;
      }
      this.sells.forEach(sell => {
        sell.person = this.client;
      });

      this.subscribeToSaveResponse(
        this.sellService.create2(this.sells, paid ?? 0, shippingCost ?? 0, this.calculeDiscountAmount(), paymentMethod)
      );
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  calculeTotal(): number {
    return this.sells.map(s => (s.quantity ?? 0) * (s.unitPrice ?? 0)).reduce((acc, value) => acc + value, 0);
  }

  calculeDiscountAmount(): number {
    const { discount, discountType } = this.editForm.value;
    const isFixedDiscount: boolean = discountType === 'fixed';
    const total: number = this.calculeTotal();
    return isFixedDiscount ? +discount : (total * discount) / 100;
  }

  calculeTotalNet(): number {
    return this.calculeTotal() - this.calculeDiscountAmount() + Number(this.editForm.get('shippingCost')?.value ?? 0);
  }

  saveIsValid(): boolean {
    let quantityValid = true;
    this.sells.forEach(sell => {
      if ((sell.product?.quantity ?? 0) < (sell.quantity ?? 0)) {
        quantityValid = false;
      }
    });
    return this.editForm.valid && !this.isSaving && quantityValid;
  }

  protected addErrorAlert(message?: string): void {
    this.alertService.addAlert({ type: 'danger', message });
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISell>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      error => this.onSaveError(error.error)
    );
  }

  protected onSaveSuccess(): void {
    this.productService.loadProductsAlert();
    this.router.navigate(['/sell']);
  }

  protected onSaveError(error: any): void {
    this.sweetAlertService.create(error.title, error.errorKey, 'error');
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .clients()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      //.pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => {
        this.peopleSharedCollection = people;
        //this.client = DEFAULT_CLIENT;
      });

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      /*.pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )*/
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
  /* isSaving = false;

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
  } */
}
