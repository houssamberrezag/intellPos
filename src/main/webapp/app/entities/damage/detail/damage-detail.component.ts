import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDamage } from '../damage.model';

@Component({
  selector: 'jhi-damage-detail',
  templateUrl: './damage-detail.component.html',
})
export class DamageDetailComponent implements OnInit {
  damage: IDamage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ damage }) => {
      this.damage = damage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
