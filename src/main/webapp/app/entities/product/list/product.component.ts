import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProduct, Product } from '../product.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ProductService } from '../service/product.service';
import { ProductDeleteDialogComponent } from '../delete/product-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { ExportExcelService } from 'app/core/util/export-excel.service';
import { FormBuilder, Validators } from '@angular/forms';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-product',
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit {
  products?: IProduct[];
  selectedProduct: IProduct | null = null;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  countOfTransactionAtSelectedProduct = 0;
  editPriceForm = this.fb.group({
    costPrice: [null, [Validators.required, Validators.min(0)]],
    minimumRetailPrice: [null, [Validators.required, Validators.min(0)]],
  });

  constructor(
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    private exportExcelService: ExportExcelService,
    private fb: FormBuilder,
    private alertService: SweetAlertService,
    private transactionService: TransactionService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.productService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IProduct[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }
  setSelectedProduct(product: IProduct, updatePrice?: boolean): void {
    this.selectedProduct = product;
    if (updatePrice) {
      this.editPriceForm.patchValue({
        costPrice: product.costPrice,
        minimumRetailPrice: product.minimumRetailPrice,
      });
    } else {
      this.loadCountTransactions(product.id ?? -1);
    }
  }

  saveUpdatePrice(): void {
    if (this.editPriceForm.valid && this.selectedProduct) {
      const { costPrice, minimumRetailPrice } = this.editPriceForm.value;
      this.selectedProduct.costPrice = costPrice;
      this.selectedProduct.minimumRetailPrice = minimumRetailPrice;
      const patchObject = Object.assign({
        id: this.selectedProduct.id,
        costPrice,
        minimumRetailPrice,
      });
      this.productService
        .partialUpdate({
          id: this.selectedProduct.id,
          costPrice,
          minimumRetailPrice,
        })
        .subscribe(res => {
          document.getElementById('closeModal')?.click();
          this.alertService.create('', 'prix modifié avec succées', 'success');
        });
    }
  }

  loadCountTransactions(productId: number): void {
    this.transactionService.countByProductId(productId).subscribe(
      count => (this.countOfTransactionAtSelectedProduct = count ?? 0),
      () => (this.countOfTransactionAtSelectedProduct = 0)
    );
  }
  deleteProduct(): void {
    if (this.selectedProduct && this.countOfTransactionAtSelectedProduct === 0) {
      this.productService
        .partialUpdate({
          id: this.selectedProduct.id,
          deletedAt: dayjs(),
        })
        .subscribe(res => {
          this.loadPage();
          document.getElementById('closeModal2')?.click();
        });
    }
  }

  trackId(index: number, item: IProduct): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(product: IProduct): void {
    const modalRef = this.modalService.open(ProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.product = product;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  exportexcel(): void {
    const fileName = 'produits.xlsx';
    this.exportExcelService.export(this.dataToExport(), fileName);
  }

  dataToExport(): any[] {
    if (this.products) {
      return this.products?.map(p => ({
        Nom: p.name,
        code: p.code,
        'Quantité en stock': p.quantity,
        'Prix de revient': p.costPrice,
        'Prix de vente': p.minimumRetailPrice,
        Unité: p.unit,
      }));
    } else {
      return [];
    }
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IProduct[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/product'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.products = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
