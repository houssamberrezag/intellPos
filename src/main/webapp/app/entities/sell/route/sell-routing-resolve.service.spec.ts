jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISell, Sell } from '../sell.model';
import { SellService } from '../service/sell.service';

import { SellRoutingResolveService } from './sell-routing-resolve.service';

describe('Service Tests', () => {
  describe('Sell routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SellRoutingResolveService;
    let service: SellService;
    let resultSell: ISell | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SellRoutingResolveService);
      service = TestBed.inject(SellService);
      resultSell = undefined;
    });

    describe('resolve', () => {
      it('should return ISell returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSell = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSell).toEqual({ id: 123 });
      });

      it('should return new ISell if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSell = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSell).toEqual(new Sell());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Sell })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSell = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSell).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
