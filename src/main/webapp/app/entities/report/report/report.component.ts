import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss'],
})
export class ReportComponent implements OnInit {
  pageable: any = { page: 0, size: 50 };
  products: IProduct[] = [];

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.productService.query(this.pageable).subscribe(
      res => {
        this.products = res.body ?? [];
      },
      () => {
        this.products = [];
      }
    );
  }

  generateProductsReport(): void {
    this.router.navigate(['/report/products'], { state: { example: 'bar' } });
  }
}
