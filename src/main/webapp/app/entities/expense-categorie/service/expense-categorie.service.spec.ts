import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpenseCategorie, ExpenseCategorie } from '../expense-categorie.model';

import { ExpenseCategorieService } from './expense-categorie.service';

describe('Service Tests', () => {
  describe('ExpenseCategorie Service', () => {
    let service: ExpenseCategorieService;
    let httpMock: HttpTestingController;
    let elemDefault: IExpenseCategorie;
    let expectedResult: IExpenseCategorie | IExpenseCategorie[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExpenseCategorieService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        createdAt: currentDate,
        updatedAt: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ExpenseCategorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new ExpenseCategorie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExpenseCategorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ExpenseCategorie', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new ExpenseCategorie()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExpenseCategorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ExpenseCategorie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExpenseCategorieToCollectionIfMissing', () => {
        it('should add a ExpenseCategorie to an empty array', () => {
          const expenseCategorie: IExpenseCategorie = { id: 123 };
          expectedResult = service.addExpenseCategorieToCollectionIfMissing([], expenseCategorie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(expenseCategorie);
        });

        it('should not add a ExpenseCategorie to an array that contains it', () => {
          const expenseCategorie: IExpenseCategorie = { id: 123 };
          const expenseCategorieCollection: IExpenseCategorie[] = [
            {
              ...expenseCategorie,
            },
            { id: 456 },
          ];
          expectedResult = service.addExpenseCategorieToCollectionIfMissing(expenseCategorieCollection, expenseCategorie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExpenseCategorie to an array that doesn't contain it", () => {
          const expenseCategorie: IExpenseCategorie = { id: 123 };
          const expenseCategorieCollection: IExpenseCategorie[] = [{ id: 456 }];
          expectedResult = service.addExpenseCategorieToCollectionIfMissing(expenseCategorieCollection, expenseCategorie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(expenseCategorie);
        });

        it('should add only unique ExpenseCategorie to an array', () => {
          const expenseCategorieArray: IExpenseCategorie[] = [{ id: 123 }, { id: 456 }, { id: 94648 }];
          const expenseCategorieCollection: IExpenseCategorie[] = [{ id: 123 }];
          expectedResult = service.addExpenseCategorieToCollectionIfMissing(expenseCategorieCollection, ...expenseCategorieArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const expenseCategorie: IExpenseCategorie = { id: 123 };
          const expenseCategorie2: IExpenseCategorie = { id: 456 };
          expectedResult = service.addExpenseCategorieToCollectionIfMissing([], expenseCategorie, expenseCategorie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(expenseCategorie);
          expect(expectedResult).toContain(expenseCategorie2);
        });

        it('should accept null and undefined values', () => {
          const expenseCategorie: IExpenseCategorie = { id: 123 };
          expectedResult = service.addExpenseCategorieToCollectionIfMissing([], null, expenseCategorie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(expenseCategorie);
        });

        it('should return initial array if no ExpenseCategorie is added', () => {
          const expenseCategorieCollection: IExpenseCategorie[] = [{ id: 123 }];
          expectedResult = service.addExpenseCategorieToCollectionIfMissing(expenseCategorieCollection, undefined, null);
          expect(expectedResult).toEqual(expenseCategorieCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
