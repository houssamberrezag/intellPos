jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITaxe, Taxe } from '../taxe.model';
import { TaxeService } from '../service/taxe.service';

import { TaxeRoutingResolveService } from './taxe-routing-resolve.service';

describe('Service Tests', () => {
  describe('Taxe routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TaxeRoutingResolveService;
    let service: TaxeService;
    let resultTaxe: ITaxe | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TaxeRoutingResolveService);
      service = TestBed.inject(TaxeService);
      resultTaxe = undefined;
    });

    describe('resolve', () => {
      it('should return ITaxe returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaxe = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTaxe).toEqual({ id: 123 });
      });

      it('should return new ITaxe if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaxe = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTaxe).toEqual(new Taxe());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Taxe })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaxe = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTaxe).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
