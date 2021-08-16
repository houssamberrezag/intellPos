import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISell } from '../sell.model';

@Component({
  selector: 'jhi-sell-detail',
  templateUrl: './sell-detail.component.html',
})
export class SellDetailComponent implements OnInit {
  sell: ISell | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sell }) => {
      this.sell = sell;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
