jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISubcategorie, Subcategorie } from '../subcategorie.model';
import { SubcategorieService } from '../service/subcategorie.service';

import { SubcategorieRoutingResolveService } from './subcategorie-routing-resolve.service';

describe('Service Tests', () => {
  describe('Subcategorie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SubcategorieRoutingResolveService;
    let service: SubcategorieService;
    let resultSubcategorie: ISubcategorie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SubcategorieRoutingResolveService);
      service = TestBed.inject(SubcategorieService);
      resultSubcategorie = undefined;
    });

    describe('resolve', () => {
      it('should return ISubcategorie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubcategorie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubcategorie).toEqual({ id: 123 });
      });

      it('should return new ISubcategorie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubcategorie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSubcategorie).toEqual(new Subcategorie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Subcategorie })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubcategorie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubcategorie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
