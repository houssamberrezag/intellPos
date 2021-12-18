import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-product-report',
  templateUrl: './product-report.component.html',
  styleUrls: ['./product-report.component.scss'],
})
export class ProductReportComponent implements OnInit {
  constructor(private router: Router) {
    console.log(this.router.getCurrentNavigation()?.extras?.state?.example);
  }

  ngOnInit(): void {
    console.log('');
  }
}
