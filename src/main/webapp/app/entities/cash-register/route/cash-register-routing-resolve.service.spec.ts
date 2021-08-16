jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICashRegister, CashRegister } from '../cash-register.model';
import { CashRegisterService } from '../service/cash-register.service';

import { CashRegisterRoutingResolveService } from './cash-register-routing-resolve.service';

describe('Service Tests', () => {
  describe('CashRegister routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CashRegisterRoutingResolveService;
    let service: CashRegisterService;
    let resultCashRegister: ICashRegister | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CashRegisterRoutingResolveService);
      service = TestBed.inject(CashRegisterService);
      resultCashRegister = undefined;
    });

    describe('resolve', () => {
      it('should return ICashRegister returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCashRegister = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCashRegister).toEqual({ id: 123 });
      });

      it('should return new ICashRegister if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCashRegister = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCashRegister).toEqual(new CashRegister());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CashRegister })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCashRegister = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCashRegister).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
