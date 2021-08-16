import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpenseCategorie } from '../expense-categorie.model';

@Component({
  selector: 'jhi-expense-categorie-detail',
  templateUrl: './expense-categorie-detail.component.html',
})
export class ExpenseCategorieDetailComponent implements OnInit {
  expenseCategorie: IExpenseCategorie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseCategorie }) => {
      this.expenseCategorie = expenseCategorie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
