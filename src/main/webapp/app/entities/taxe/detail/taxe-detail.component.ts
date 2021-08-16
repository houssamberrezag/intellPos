import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaxe } from '../taxe.model';

@Component({
  selector: 'jhi-taxe-detail',
  templateUrl: './taxe-detail.component.html',
})
export class TaxeDetailComponent implements OnInit {
  taxe: ITaxe | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxe }) => {
      this.taxe = taxe;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
