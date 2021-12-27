import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';
import { PaymentTypes } from 'app/entities/enumerations/payment-types.model';
import { PersonTypes } from 'app/entities/enumerations/person-types.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { PurchaseService } from 'app/entities/purchase/service/purchase.service';
import { SellService } from 'app/entities/sell/service/sell.service';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import * as dayjs from 'dayjs';

import { IPerson } from '../person.model';
import { PaymentBillService } from 'app/entities/payment/service/payment-bill.service';

@Component({
  selector: 'jhi-person-detail',
  templateUrl: './person-detail.component.html',
})
export class PersonDetailComponent implements OnInit {
  person: IPerson | null = null;
  personType = PersonTypes;
  totalTransactionsAmount = 0;
  totalPaymentsAmount = 0;
  quantity = 0;
  countTransactions = 0;
  payments: IPayment[] = [];

  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  totalItems = 0;
  ngbPaginationPage = 1;

  paymentForm: FormGroup = this.fb.group({
    amount: [null, [Validators.required]],
    method: [null, [Validators.maxLength(191)]],
    note: [null, [Validators.required, Validators.maxLength(191)]],
    date: [null, [Validators.required]],
  });

  constructor(
    private fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    private sellService: SellService,
    private purchaseService: PurchaseService,
    private transactionService: TransactionService,
    private paymentService: PaymentService,
    private alertService: SweetAlertService,
    private billService: PaymentBillService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
      this.loadData();
      this.loadPayments();
    });
  }

  previousState(): void {
    window.history.back();
  }

  loadData(): void {
    if (this.person?.id) {
      this.transactionService.countByPersonId(this.person.id).subscribe(data => (this.countTransactions = data));
      this.transactionService.totalAmountByPersonId(this.person.id).subscribe(data => (this.totalTransactionsAmount = data));
      this.paymentService.totalAmountByPersonId(this.person.id).subscribe(data => (this.totalPaymentsAmount = data));

      if (this.person.personType === this.personType.CLIENT) {
        this.sellService.totalQuantity(this.person.id).subscribe(data => (this.quantity = data));
      } else {
        this.purchaseService.totalQuantity(this.person.id).subscribe(data => (this.quantity = data));
      }
    }
  }

  loadPayments(page?: number): void {
    if (this.person?.id) {
      const pageToLoad = page ?? this.page ?? 0;
      this.paymentService
        .findByPersonId(this.person.id, {
          page: pageToLoad,
          size: this.itemsPerPage,
          sort: ['id,desc'],
        })
        .subscribe(
          res => {
            this.onSuccess(res.body, res.headers, pageToLoad);
          },
          () => {
            this.onError();
          }
        );
    }
  }
  loadPage(event: any): void {
    this.page = event - 1;
    this.loadPayments();
  }

  totalDue(): number {
    return (this.person?.proviousDue ?? 0) + (this.totalTransactionsAmount ?? 0) - (this.totalPaymentsAmount ?? 0);
  }

  savePayment(): void {
    if (this.paymentValid()) {
      const payment = this.createFromForm();
      this.paymentService.create(payment).subscribe(res => {
        this.loadPayments();
        this.loadData();
        this.alertService.create('paiement effectué', 'paiement effectué avec succés', 'success');
        this.initPaymentForm();
      });
    }
  }

  initPaymentForm(): void {
    this.paymentForm.setValue({
      amount: null,
      method: null,
      note: null,
      date: null,
    });
  }

  paymentValid(): boolean {
    return this.paymentForm.valid && (this.paymentForm.get(['amount'])?.value ?? 0) <= this.totalDue();
  }

  generateBill(payment: IPayment): void {
    this.billService.generatePdf(payment);
  }

  protected createFromForm(): IPayment {
    if (this.person) {
      return {
        amount: this.paymentForm.get(['amount'])!.value,
        method: this.paymentForm.get(['method'])!.value,
        type: this.person?.personType === this.personType.CLIENT ? PaymentTypes.CREDIT : PaymentTypes.DEBIT,
        //referenceNo: ,
        note: this.paymentForm.get(['note'])!.value,
        date: this.paymentForm.get(['date'])!.value ? dayjs(this.paymentForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
        person: this.person,
      };
    } else {
      return {};
    }
  }

  protected onSuccess(data: IPayment[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    //this.page = page;

    this.payments = data ?? [];
    //this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
