import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TodayPaymentsComponent } from './today-payments.component';

describe('TodayPaymentsComponent', () => {
  let component: TodayPaymentsComponent;
  let fixture: ComponentFixture<TodayPaymentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TodayPaymentsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TodayPaymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
