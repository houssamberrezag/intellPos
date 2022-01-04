import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { Chart, ChartItem } from 'chart.js';
import { SellService } from 'app/entities/sell/service/sell.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  sellTotayData: { quantite?: number; total?: number } = {};

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private sellService: SellService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        if (!account) {
          this.login();
        }
        this.account = account;
      });
    this.profitsChart();
    this.loadData();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadData(): void {
    this.sellService.findTodaySumQuantitesAndItems().subscribe(data => (this.sellTotayData = data));
  }

  profitsChart(): void {
    const profitChart = document.getElementById('profit');
    const profits = [0, 3900, 0, 8600, 2790, 8600];
    const chart = new Chart(profitChart as ChartItem, {
      type: 'line',
      data: {
        labels: ['Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        datasets: [
          {
            label: "{{trans('core.profit')}}",
            data: profits,
            borderColor: 'rgba(102,165,226, 0.2)',
            backgroundColor: 'rgba(102,165,226, 0.7)',
            pointBorderColor: 'rgba(102,165,226, 0.5)',
            pointBackgroundColor: 'rgba(102,165,226, 0.2)',
            pointBorderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        //legend: false,
      },
    });
  }
}
