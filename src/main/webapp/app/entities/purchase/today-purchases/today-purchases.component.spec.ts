import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TodayPurchasesComponent } from './today-purchases.component';

describe('TodayPurchasesComponent', () => {
  let component: TodayPurchasesComponent;
  let fixture: ComponentFixture<TodayPurchasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TodayPurchasesComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TodayPurchasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
