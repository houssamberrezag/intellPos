import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductComponent } from './list/product.component';
import { ProductDetailComponent } from './detail/product-detail.component';
import { ProductUpdateComponent } from './update/product-update.component';
import { ProductDeleteDialogComponent } from './delete/product-delete-dialog.component';
import { ProductRoutingModule } from './route/product-routing.module';
import { PrintSingleBarcodeComponent } from './print-single-barcode/print-single-barcode.component';
import { NgxBarcodeModule } from 'ngx-barcode';
import { ProductAlertComponent } from './product-alert/product-alert.component';

@NgModule({
  imports: [SharedModule, ProductRoutingModule, NgxBarcodeModule],
  declarations: [
    ProductComponent,
    ProductDetailComponent,
    ProductUpdateComponent,
    ProductDeleteDialogComponent,
    PrintSingleBarcodeComponent,
    ProductAlertComponent,
  ],
  entryComponents: [ProductDeleteDialogComponent],
})
export class ProductModule {}
