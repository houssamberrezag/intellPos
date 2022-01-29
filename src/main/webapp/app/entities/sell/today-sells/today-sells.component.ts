import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { TransactionDeleteDialogComponent } from 'app/entities/transaction/delete/transaction-delete-dialog.component';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { SellBillService } from '../service/sell-bill.service';

@Component({
  selector: 'jhi-today-sells',
  templateUrl: './today-sells.component.html',
  styleUrls: ['./today-sells.component.scss'],
})
export class TodaySellsComponent implements OnInit {
  transactions?: ITransaction[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  resume: { total: number; paid: number } = { total: 0, paid: 0 };
  showenSummary = false;
  constructor(
    protected transactionService: TransactionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    private billService: SellBillService
  ) {}

  ngOnInit(): void {
    this.handleNavigation();
    this.loadResume();
  }

  loadResume(): void {
    this.transactionService.sellTodayResume().subscribe(res => {
      if (res) {
        this.resume = res;
      }
    });
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    this.transactionService
      .todaySells({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ITransaction[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  trackId(index: number, item: ITransaction): number {
    return item.id!;
  }

  delete(transaction: ITransaction): void {
    const modalRef = this.modalService.open(TransactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.transaction = transaction;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  generateBill(transaction: ITransaction): void {
    this.billService.generatePdf(transaction);
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

  protected onSuccess(data: ITransaction[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      const route = ['/sell/today'];
      this.router.navigate(route, {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.transactions = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
