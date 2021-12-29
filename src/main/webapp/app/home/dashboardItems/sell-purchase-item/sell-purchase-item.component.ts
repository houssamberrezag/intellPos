import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';

@Component({
  selector: 'jhi-sell-purchase-item',
  templateUrl: './sell-purchase-item.component.html',
  styleUrls: ['./sell-purchase-item.component.scss'],
})
export class SellPurchaseItemComponent implements OnInit {
  show = false;
  public barChartOptions: ChartOptions = {
    responsive: true,
  };
  public barChartLabels = ['Aoùt', 'Sep', 'Oct', 'Nov', 'Déc'];
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;
  public barChartPlugins = [];

  public barChartData: any[] = [
    { data: [65, 59, 80, 81, 56], label: 'Sells', backgroundColor: '#039A93' },
    { data: [28, 48, 40, 19, 86], label: 'Purchases', backgroundColor: '#58D68D' },
  ];

  // constructor() { }

  ngOnInit(): void {
    console.log('init');
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.show = true;
    }, 400);
  }
}
