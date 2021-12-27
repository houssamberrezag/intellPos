import { TestBed } from '@angular/core/testing';

import { SellBillService } from './sell-bill.service';

describe('SellBillService', () => {
  let service: SellBillService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SellBillService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
