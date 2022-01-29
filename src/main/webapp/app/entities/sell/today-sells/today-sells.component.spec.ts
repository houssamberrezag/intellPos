import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TodaySellsComponent } from './today-sells.component';

describe('TodaySellsComponent', () => {
  let component: TodaySellsComponent;
  let fixture: ComponentFixture<TodaySellsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TodaySellsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TodaySellsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
