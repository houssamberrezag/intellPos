import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProduct } from '../product.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { ISell } from 'app/entities/sell/sell.model';
import { IPurchase } from 'app/entities/purchase/purchase.model';
import { SellService } from 'app/entities/sell/service/sell.service';
import { PurchaseService } from 'app/entities/purchase/service/purchase.service';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
})
export class ProductDetailComponent implements OnInit {
  product: IProduct | null = null;
  sellTansactions: ITransaction[] = [];
  purchaseTansactions: ITransaction[] = [];
  sells: ISell[] = [];
  purchases: IPurchase[] = [];

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    private transactionService: TransactionService,
    private sellService: SellService,
    private purchaseService: PurchaseService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  totalSellsQte(): number {
    return this.sells.map(sell => sell.quantity ?? 0).reduce((a, b) => a + b, 0);
  }

  totalPurchasesQte(): number {
    return this.purchases.map(purchase => purchase.quantity ?? 0).reduce((a, b) => a + b, 0);
  }

  totalSellsQteOfTansaction(referenceNo: string): number {
    return this.sells
      .filter(sell => sell.referenceNo === referenceNo)
      .map(sell => sell.quantity ?? 0)
      .reduce((a, b) => a + b, 0);
  }

  totalPurchasesQteOfTansaction(referenceNo: string): number {
    return this.purchases
      .filter(sell => sell.referenceNo === referenceNo)
      .map(purchase => purchase.quantity ?? 0)
      .reduce((a, b) => a + b, 0);
  }

  loadRelationshipsOptions(): void {
    if (this.product?.id) {
      this.transactionService.transactionsPurchaseByProductId(this.product.id).subscribe(res => {
        this.purchaseTansactions = res.body ?? [];
      });

      this.transactionService.transactionsSellByProductId(this.product.id).subscribe(res => {
        this.sellTansactions = res.body ?? [];
      });

      this.sellService.findByProductId(this.product.id).subscribe(res => {
        this.sells = res.body ?? [];
      });

      this.purchaseService.findByProductId(this.product.id).subscribe(res => {
        this.purchases = res.body ?? [];
      });
    }
  }
}
