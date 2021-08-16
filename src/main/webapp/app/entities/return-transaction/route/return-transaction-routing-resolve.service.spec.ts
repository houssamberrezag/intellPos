jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReturnTransaction, ReturnTransaction } from '../return-transaction.model';
import { ReturnTransactionService } from '../service/return-transaction.service';

import { ReturnTransactionRoutingResolveService } from './return-transaction-routing-resolve.service';

describe('Service Tests', () => {
  describe('ReturnTransaction routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ReturnTransactionRoutingResolveService;
    let service: ReturnTransactionService;
    let resultReturnTransaction: IReturnTransaction | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ReturnTransactionRoutingResolveService);
      service = TestBed.inject(ReturnTransactionService);
      resultReturnTransaction = undefined;
    });

    describe('resolve', () => {
      it('should return IReturnTransaction returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnTransaction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReturnTransaction).toEqual({ id: 123 });
      });

      it('should return new IReturnTransaction if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnTransaction = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultReturnTransaction).toEqual(new ReturnTransaction());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReturnTransaction })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnTransaction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReturnTransaction).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
