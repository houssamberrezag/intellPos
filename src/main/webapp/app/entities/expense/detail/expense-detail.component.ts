import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpense } from '../expense.model';

@Component({
  selector: 'jhi-expense-detail',
  templateUrl: './expense-detail.component.html',
})
export class ExpenseDetailComponent implements OnInit {
  expense: IExpense | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.expense = expense;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
