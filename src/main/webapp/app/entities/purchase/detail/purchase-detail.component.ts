import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { ITransaction } from 'app/entities/transaction/transaction.model';

import { IPurchase } from '../purchase.model';
import { PurchaseService } from '../service/purchase.service';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { PaymentTypes } from 'app/entities/enumerations/payment-types.model';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';

declare let $: any;

@Component({
  selector: 'jhi-purchase-detail',
  templateUrl: './purchase-detail.component.html',
})
export class PurchaseDetailComponent implements OnInit {
  purchases: IPurchase[] | null = [];
  payments: IPayment[] = [];
  transaction: ITransaction | null = null;

  paymentForm: FormGroup = this.fb.group({
    amount: [null, [Validators.required]],
    method: [null, [Validators.maxLength(191)]],
    note: [null, [Validators.required, Validators.maxLength(191)]],
    date: [null, [Validators.required]],
  });

  constructor(
    protected activatedRoute: ActivatedRoute,
    private purchaseService: PurchaseService,
    private paymentService: PaymentService,
    private fb: FormBuilder,
    private alertService: SweetAlertService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      this.loadPurchases();
      this.loadPayments();
    });
  }

  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
    console.log('ngAfterViewInit');
    $('.paymentTime').datetimepicker({
      format: 'YYYY-M-D H:mm:ss',
    });
  }

  loadPurchases(): void {
    this.purchaseService.getPurchasesByReference(this.transaction?.referenceNo ?? '').subscribe(data => {
      this.purchases = data.body;
    });
  }

  loadPayments(): void {
    this.paymentService.getPaymentsByReference(this.transaction?.referenceNo ?? '').subscribe(data => {
      this.payments = data.body ?? [];
    });
  }

  totalQuantities(): number {
    return this.purchases?.map(p => p.quantity).reduce((a, b) => (a ?? 0) + (b ?? 0), 0) ?? 0;
  }

  totalPayments(): string {
    return (this.payments.map(p => p.amount).reduce((a, b) => (a ?? 0) + (b ?? 0), 0) ?? 0).toFixed(2);
  }

  savePayment(): void {
    if (this.paymentValid()) {
      const payment = this.createFromForm();
      this.paymentService.create(payment).subscribe(res => {
        this.loadPayments();
        if (this.transaction?.paid) {
          this.transaction.paid += payment.amount ?? 0;
        }
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
  previousState(): void {
    window.history.back();
  }

  paymentValid(): boolean {
    return (
      this.paymentForm.valid &&
      (this.paymentForm.get(['amount'])?.value ?? 0) <= (this.transaction?.netTotal ?? 0) - (this.transaction?.paid ?? 0)
    );
  }

  protected createFromForm(): IPayment {
    console.log(this.paymentForm.get(['date'])!.value);
    return {
      amount: this.paymentForm.get(['amount'])!.value,
      method: this.paymentForm.get(['method'])!.value,
      type: PaymentTypes.DEBIT,
      referenceNo: this.transaction?.referenceNo,
      note: this.paymentForm.get(['note'])!.value,
      date: this.paymentForm.get(['date'])!.value ? dayjs(this.paymentForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      person: this.transaction?.person ?? {},
    };
  }
}
