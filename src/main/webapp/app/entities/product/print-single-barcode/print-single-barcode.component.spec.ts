import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintSingleBarcodeComponent } from './print-single-barcode.component';

describe('PrintSingleBarcodeComponent', () => {
  let component: PrintSingleBarcodeComponent;
  let fixture: ComponentFixture<PrintSingleBarcodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PrintSingleBarcodeComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrintSingleBarcodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
