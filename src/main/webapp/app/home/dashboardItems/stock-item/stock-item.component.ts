import { Component } from '@angular/core';
import { ChartType, ChartOptions } from 'chart.js';

@Component({
  selector: 'jhi-stock-item',
  templateUrl: './stock-item.component.html',
  styleUrls: ['./stock-item.component.scss'],
})
export class StockItemComponent {
  // Pie
  public pieChartOptions: ChartOptions = {
    responsive: true,
  };
  public pieChartLabels = [['Download', 'Sales'], ['In', 'Store', 'Sales'], 'Mail Sales'];
  public pieChartData: any[] = [300, 500, 100];
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartPlugins = [];

  /*constructor() {
    //monkeyPatchChartJsTooltip();
    //monkeyPatchChartJsLegend();
  }*/
}
