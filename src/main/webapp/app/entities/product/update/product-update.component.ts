import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { ISubcategorie } from 'app/entities/subcategorie/subcategorie.model';
import { SubcategorieService } from 'app/entities/subcategorie/service/subcategorie.service';
import { ITaxe } from 'app/entities/taxe/taxe.model';
import { TaxeService } from 'app/entities/taxe/service/taxe.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategorie[] = [];
  subcategoriesSharedCollection: ISubcategorie[] = [];
  taxesSharedCollection: ITaxe[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    code: [null, [Validators.required, Validators.maxLength(10)]],
    quantity: [null],
    details: [],
    costPrice: [null, [Validators.required]],
    minimumRetailPrice: [null, [Validators.required]],
    unit: [null, [Validators.required]],
    status: [true, Validators.required],
    image: [null, [Validators.maxLength(255)]],
    openingStock: [0, [Validators.required, Validators.min(0)]],
    alertQuantity: [0, [Validators.required, Validators.min(0)]],
    /* createdAt: [],
    updatedAt: [],
    deletedAt: [], */
    categorie: [null, Validators.required],
    subCategorie: [],
    taxe: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productService: ProductService,
    protected categorieService: CategorieService,
    protected subcategorieService: SubcategorieService,
    protected taxeService: TaxeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      /*if (product.id === undefined) {
        const today = dayjs().startOf('day');
        product.createdAt = today;
        product.updatedAt = today;
        product.deletedAt = today;
      }*/

      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('intellposApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product: IProduct = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      product.quantity = product.openingStock;
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  getFiltredSubCategorys(): ISubcategorie[] {
    return this.subcategoriesSharedCollection.filter(subC => subC.categorie?.id === this.editForm.get(['categorie'])!.value?.id);
  }

  uploadPhoto(event: any): void {
    console.log('houssam');
  }

  trackCategorieById(index: number, item: ICategorie): number {
    return item.id!;
  }

  trackSubcategorieById(index: number, item: ISubcategorie): number {
    return item.id!;
  }

  trackTaxeById(index: number, item: ITaxe): number {
    return item.id!;
  }

  generateCode(): void {
    const productName = this.editForm.get(['name'])!.value;
    if (productName) {
      this.editForm.patchValue({
        code: `${String(productName.substring(0, 2)).toUpperCase()}${String(Math.floor(Math.random() * 1000) + 999)}`,
      });
    } else {
      this.editForm.patchValue({
        code: Math.floor(Math.random() * 90000) + 100000,
      });
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      name: product.name,
      code: product.code,
      quantity: product.quantity,
      details: product.details,
      costPrice: product.costPrice,
      minimumRetailPrice: product.minimumRetailPrice,
      unit: product.unit,
      status: product.status,
      image: product.image,
      openingStock: product.openingStock,
      alertQuantity: product.alertQuantity,
      /* createdAt: product.createdAt ? product.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: product.updatedAt ? product.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: product.deletedAt ? product.deletedAt.format(DATE_TIME_FORMAT) : null, */
      categorie: product.categorie,
      subCategorie: product.subCategorie,
      taxe: product.taxe,
    });

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      product.categorie
    );
    this.subcategoriesSharedCollection = this.subcategorieService.addSubcategorieToCollectionIfMissing(
      this.subcategoriesSharedCollection,
      product.subCategorie
    );
    this.taxesSharedCollection = this.taxeService.addTaxeToCollectionIfMissing(this.taxesSharedCollection, product.taxe);
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));

    this.subcategorieService
      .query()
      .pipe(map((res: HttpResponse<ISubcategorie[]>) => res.body ?? []))
      .pipe(
        map((subcategories: ISubcategorie[]) =>
          this.subcategorieService.addSubcategorieToCollectionIfMissing(subcategories, this.editForm.get('subCategorie')!.value)
        )
      )
      .subscribe((subcategories: ISubcategorie[]) => (this.subcategoriesSharedCollection = subcategories));

    this.taxeService
      .query()
      .pipe(map((res: HttpResponse<ITaxe[]>) => res.body ?? []))
      .pipe(map((taxes: ITaxe[]) => this.taxeService.addTaxeToCollectionIfMissing(taxes, this.editForm.get('taxe')!.value)))
      .subscribe((taxes: ITaxe[]) => (this.taxesSharedCollection = taxes));
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      code: this.editForm.get(['code'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      details: this.editForm.get(['details'])!.value,
      costPrice: this.editForm.get(['costPrice'])!.value,
      minimumRetailPrice: this.editForm.get(['minimumRetailPrice'])!.value,
      unit: this.editForm.get(['unit'])!.value,
      status: this.editForm.get(['status'])!.value,
      image: this.editForm.get(['image'])!.value,
      openingStock: this.editForm.get(['openingStock'])!.value,
      alertQuantity: this.editForm.get(['alertQuantity'])!.value,

      categorie: this.editForm.get(['categorie'])!.value,
      subCategorie: this.editForm.get(['subCategorie'])!.value,
      taxe: this.editForm.get(['taxe'])!.value,
    };
  }
}
