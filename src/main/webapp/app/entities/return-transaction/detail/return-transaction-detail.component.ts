import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReturnTransaction } from '../return-transaction.model';

@Component({
  selector: 'jhi-return-transaction-detail',
  templateUrl: './return-transaction-detail.component.html',
})
export class ReturnTransactionDetailComponent implements OnInit {
  returnTransaction: IReturnTransaction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnTransaction }) => {
      this.returnTransaction = returnTransaction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
