jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITransaction, Transaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';

import { TransactionRoutingResolveService } from './transaction-routing-resolve.service';

describe('Service Tests', () => {
  describe('Transaction routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TransactionRoutingResolveService;
    let service: TransactionService;
    let resultTransaction: ITransaction | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TransactionRoutingResolveService);
      service = TestBed.inject(TransactionService);
      resultTransaction = undefined;
    });

    describe('resolve', () => {
      it('should return ITransaction returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTransaction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTransaction).toEqual({ id: 123 });
      });

      it('should return new ITransaction if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTransaction = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTransaction).toEqual(new Transaction());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Transaction })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTransaction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTransaction).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
