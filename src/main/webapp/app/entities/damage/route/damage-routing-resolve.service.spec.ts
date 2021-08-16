jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDamage, Damage } from '../damage.model';
import { DamageService } from '../service/damage.service';

import { DamageRoutingResolveService } from './damage-routing-resolve.service';

describe('Service Tests', () => {
  describe('Damage routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DamageRoutingResolveService;
    let service: DamageService;
    let resultDamage: IDamage | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DamageRoutingResolveService);
      service = TestBed.inject(DamageService);
      resultDamage = undefined;
    });

    describe('resolve', () => {
      it('should return IDamage returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDamage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDamage).toEqual({ id: 123 });
      });

      it('should return new IDamage if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDamage = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDamage).toEqual(new Damage());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Damage })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDamage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDamage).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
