import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from 'app/core/util/alert.service';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { PersonTypes } from 'app/entities/enumerations/person-types.model';
import { IPerson, Person } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IProduct, Product } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { DEFAULT_CLIENT } from 'app/shared/constants/constants';
import { Observable, Subscription } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { ISell, Sell } from '../sell.model';
import { SellService } from '../service/sell.service';
declare let $: any;
@Component({
  selector: 'jhi-pos',
  templateUrl: './pos.component.html',
  styleUrls: ['./pos.component.scss'],
})
export class PosComponent implements OnInit, OnDestroy {
  clients: IPerson[] = [];
  selectedClient: IPerson = {};
  products: Product[] = [];
  sells: Sell[] = [];
  categories: ICategorie[] = [];
  loading = false;
  findByCodeSubcribtion: Subscription | null = null;
  discount = 0;

  paidAmount = 0;
  paymentMethod = 'cash';
  isSaving = false;
  clientForm: FormGroup = this.formBuilder.group({
    firstName: [null, [Validators.required, Validators.maxLength(191)]],
    lastName: [null, [Validators.required, Validators.maxLength(191)]],
    companyName: [null, [Validators.maxLength(191)]],
    email: [null, [Validators.maxLength(191)]],
    phone: [null, [Validators.required, Validators.maxLength(191)]],
    address: [null, [Validators.maxLength(191)]],
  });

  constructor(
    private formBuilder: FormBuilder,
    private personService: PersonService,
    private productService: ProductService,
    private categorieService: CategorieService,
    private sellService: SellService,
    private router: Router,
    private sweetAlertService: SweetAlertService,
    private alertService: AlertService
  ) {}
  ngOnDestroy(): void {
    this.showSideBar();
  }

  ngOnInit(): void {
    this.hideSideBar();
    this.loadRelationshipsOptions();
    this.loadProducts();
  }

  hideSideBar(): void {
    $('body').addClass('closed-sidebar');
    $('#close-sidebar').addClass('hidden');
  }

  showSideBar(): void {
    $('body').removeClass('closed-sidebar');
    $('#close-sidebar').removeClass('hidden');
  }

  initClientForm(): void {
    this.clientForm.patchValue({
      firstName: '',
      lastName: '',
      companyName: '',
      email: '',
      phone: '',
      address: '',
    });
  }
  addClient(): void {
    const person = this.createFromForm();
    this.subscribeToSaveResponseClient(this.personService.create(person));
  }

  onBarcodeScan(event: any): void {
    const code = event.target.value;
    this.findByCodeSubcribtion = this.productService.findByCode(code).subscribe(res => {
      if (res.body) {
        this.addProductToList(res.body);
        event.target.value = '';
      }
    });
  }

  addProductToList(product: Product): void {
    const optionalSell = this.sells.find(p => p.product?.code === product.code);
    if (optionalSell) {
      optionalSell.quantity = optionalSell.quantity ? optionalSell.quantity + 1 : 1;
    } else {
      this.sells.push({ product, quantity: 1 });
    }
  }

  removeSell(index: number): void {
    this.sells.splice(index, 1);
  }

  setQuantity($event: any, sell: ISell): void {
    sell.quantity = +$event.target.value;
  }

  totalItems(): number {
    return this.sells.map(s => s.quantity ?? 0).reduce((a, b) => a + b, 0);
  }

  total(): string {
    return this.sells
      .map(sell => (sell.product?.minimumRetailPrice ?? 0) * (sell.quantity ?? 0))
      .reduce((a, b) => a + b, 0)
      .toFixed(2);
  }

  totalPayable(): string {
    return (+this.total() - +this.discount).toFixed(2);
  }

  loadProductsByCategorieId(catId: number): void {
    this.loading = true;
    this.productService
      .productsByCategoryId(catId)
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .subscribe((products: IProduct[]) => {
        this.products = products;
        this.loading = false;
      });
  }

  loadProducts(): void {
    this.loading = true;
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .subscribe((products: IProduct[]) => {
        this.products = products;
        this.loading = false;
      });
  }

  posValid(): boolean {
    const noHasNegatifSellQte = !this.sells.find(s => s.quantity == null || +s.quantity <= 0 || +s.quantity > (s.product?.quantity ?? 0));
    return this.sells.length > 0 && noHasNegatifSellQte && +this.totalPayable() > 0 && this.discount >= 0;
  }

  saveValid(): boolean {
    return this.posValid() && this.paidAmount >= Number(this.totalPayable()) && this.paymentMethod != null;
  }

  savePos(): void {
    if (this.saveValid()) {
      const changeAmount = +this.paidAmount - +this.totalPayable() > 0 ? +this.paidAmount - +this.totalPayable() : 0;
      if (this.selectedClient.id) {
        this.sells.forEach(sell => {
          sell.person = this.selectedClient;
        });
      }

      this.subscribeToSaveResponse(
        this.sellService.createPos(this.sells, Number(this.totalPayable()), changeAmount, this.discount, this.paymentMethod)
      );
    }
  }

  cancel(): void {
    console.log(this.discount);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .clients()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .subscribe((people: IPerson[]) => {
        this.clients = people;
        this.selectedClient = this.clients[0] ?? {};
      });

    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .subscribe((categories: ICategorie[]) => (this.categories = categories));
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
    // this.router.navigate(['/sell']);
    //this.paymentModal?.nativeElement.click();
    this.sells = [];
    document.getElementById('closeModalButton')?.click();
    this.loadProducts();
    this.sweetAlertService.create('', 'Achat éffectuée', 'success');
  }

  protected subscribeToSaveResponseClient(result: Observable<HttpResponse<IPerson>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccessClient(res.body ?? {}),
      err => this.onSaveError(err.error)
    );
  }

  protected onSaveSuccessClient(client: IPerson): void {
    //this.previousState();
    if (client.id) {
      this.clients.push(client);
      this.selectedClient = this.clients.find(c => c.id === client.id) ?? {};
    }
  }

  protected onSaveError(error: any): void {
    this.sweetAlertService.create(error.title, error.errorKey, 'error');
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      firstName: this.clientForm.get(['firstName'])!.value,
      lastName: this.clientForm.get(['lastName'])!.value,
      companyName: this.clientForm.get(['companyName'])!.value,
      email: this.clientForm.get(['email'])!.value,
      phone: this.clientForm.get(['phone'])!.value,
      address: this.clientForm.get(['address'])!.value,
      personType: PersonTypes.CLIENT,
    };
  }
}
