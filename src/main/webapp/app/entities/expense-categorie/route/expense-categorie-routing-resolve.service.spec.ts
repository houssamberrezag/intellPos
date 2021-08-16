jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExpenseCategorie, ExpenseCategorie } from '../expense-categorie.model';
import { ExpenseCategorieService } from '../service/expense-categorie.service';

import { ExpenseCategorieRoutingResolveService } from './expense-categorie-routing-resolve.service';

describe('Service Tests', () => {
  describe('ExpenseCategorie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExpenseCategorieRoutingResolveService;
    let service: ExpenseCategorieService;
    let resultExpenseCategorie: IExpenseCategorie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExpenseCategorieRoutingResolveService);
      service = TestBed.inject(ExpenseCategorieService);
      resultExpenseCategorie = undefined;
    });

    describe('resolve', () => {
      it('should return IExpenseCategorie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpenseCategorie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExpenseCategorie).toEqual({ id: 123 });
      });

      it('should return new IExpenseCategorie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpenseCategorie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExpenseCategorie).toEqual(new ExpenseCategorie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ExpenseCategorie })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpenseCategorie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExpenseCategorie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
