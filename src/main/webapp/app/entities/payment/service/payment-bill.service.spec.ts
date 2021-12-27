import { TestBed } from '@angular/core/testing';

import { PaymentBillService } from './payment-bill.service';

describe('PaymentBillService', () => {
  let service: PaymentBillService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentBillService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
