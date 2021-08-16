import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubcategorie } from '../subcategorie.model';

@Component({
  selector: 'jhi-subcategorie-detail',
  templateUrl: './subcategorie-detail.component.html',
})
export class SubcategorieDetailComponent implements OnInit {
  subcategorie: ISubcategorie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subcategorie }) => {
      this.subcategorie = subcategorie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
