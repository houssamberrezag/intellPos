import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IProduct } from '../product.model';

@Component({
  selector: 'jhi-print-single-barcode',
  templateUrl: './print-single-barcode.component.html',
  styleUrls: ['./print-single-barcode.component.scss'],
})
export class PrintSingleBarcodeComponent implements OnInit {
  product: IProduct | null = null;
  items: number;
  printPerPage = 'pp40';
  appName = true;
  productName = true;
  price = true;
  constructor(protected activatedRoute: ActivatedRoute) {
    this.items = 36;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
    });
  }

  printDiv(divId: string): void {
    const printContents = document.getElementById(divId)?.innerHTML;
    const originalContents = document.body.innerHTML;
    if (printContents) {
      document.body.innerHTML = printContents;
      window.print();
      document.body.innerHTML = originalContents;
    }
  }
}
