import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashRegister } from '../cash-register.model';

@Component({
  selector: 'jhi-cash-register-detail',
  templateUrl: './cash-register-detail.component.html',
})
export class CashRegisterDetailComponent implements OnInit {
  cashRegister: ICashRegister | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashRegister }) => {
      this.cashRegister = cashRegister;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
