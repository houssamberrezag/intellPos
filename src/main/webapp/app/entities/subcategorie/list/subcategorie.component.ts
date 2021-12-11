import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubcategorie } from '../subcategorie.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { SubcategorieService } from '../service/subcategorie.service';
import { SubcategorieDeleteDialogComponent } from '../delete/subcategorie-delete-dialog.component';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';

@Component({
  selector: 'jhi-subcategorie',
  templateUrl: './subcategorie.component.html',
})
export class SubcategorieComponent implements OnInit {
  subcategories?: ISubcategorie[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  selectedSubCategogy: ISubcategorie = {};
  selectedSubCategogyProducts: IProduct[] = [];
  constructor(
    protected subcategorieService: SubcategorieService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    private productService: ProductService,
    private alertService: SweetAlertService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.subcategorieService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ISubcategorie[]>) => {
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

  trackId(index: number, item: ISubcategorie): number {
    return item.id!;
  }

  delete(subcategorie: ISubcategorie): void {
    const modalRef = this.modalService.open(SubcategorieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.subcategorie = subcategorie;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  async prepareDelete(subCategorie: ISubcategorie): Promise<void> {
    this.selectedSubCategogyProducts = [];
    this.selectedSubCategogy = subCategorie;
    this.selectedSubCategogyProducts =
      (await this.productService.productsBySubcategoryId(String(subCategorie.id ?? -1)).toPromise()).body ?? [];
  }

  supprimer(): void {
    if (this.selectedSubCategogy.id && this.selectedSubCategogyProducts.length <= 0) {
      this.subcategorieService.delete(this.selectedSubCategogy.id).subscribe(() => {
        this.alertService.create('', 'sous categorie supprimée avec succées', 'success');
        this.loadPage();
      });
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

  protected onSuccess(data: ISubcategorie[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/subcategorie'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.subcategories = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
