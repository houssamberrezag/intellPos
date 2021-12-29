import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellPurchaseItemComponent } from './sell-purchase-item.component';

describe('SellPurchaseItemComponent', () => {
  let component: SellPurchaseItemComponent;
  let fixture: ComponentFixture<SellPurchaseItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SellPurchaseItemComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SellPurchaseItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
