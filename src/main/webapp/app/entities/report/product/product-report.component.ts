import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-product-report',
  templateUrl: './product-report.component.html',
  styleUrls: ['./product-report.component.scss'],
})
export class ProductReportComponent implements OnInit {
  reportingProducts: IProductReport[] = [];
  constructor(private router: Router, private productService: ProductService) {
    //console.log(this.router.getCurrentNavigation()?.extras?.state?.example);
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.productService.reporting().subscribe(
      res => {
        this.reportingProducts = res.body ?? [];
      },
      () => {
        this.reportingProducts = [];
      }
    );
  }

  calculateTotalProfit(): string {
    return this.reportingProducts
      .map(rp => rp.sellTotal - rp.purchasesTotal)
      .reduce((a, b) => a + b, 0)
      .toFixed(2);
  }
}

export interface IProductReport {
  id: number;
  name: string;
  unite: string;
  stock: number;
  purchasesTotal: number;
  sellTotal: number;
  purchaseQuantity: number;
  sellQuantity: number;
}
