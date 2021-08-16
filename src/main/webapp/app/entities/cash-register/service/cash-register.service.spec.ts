import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICashRegister, CashRegister } from '../cash-register.model';

import { CashRegisterService } from './cash-register.service';

describe('Service Tests', () => {
  describe('CashRegister Service', () => {
    let service: CashRegisterService;
    let httpMock: HttpTestingController;
    let elemDefault: ICashRegister;
    let expectedResult: ICashRegister | ICashRegister[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CashRegisterService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        cashInHands: 0,
        date: currentDate,
        createdAt: currentDate,
        updatedAt: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
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

      it('should create a CashRegister', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new CashRegister()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CashRegister', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cashInHands: 1,
            date: currentDate.format(DATE_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
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

      it('should partial update a CashRegister', () => {
        const patchObject = Object.assign(
          {
            cashInHands: 1,
            date: currentDate.format(DATE_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new CashRegister()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
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

      it('should return a list of CashRegister', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cashInHands: 1,
            date: currentDate.format(DATE_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
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

      it('should delete a CashRegister', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCashRegisterToCollectionIfMissing', () => {
        it('should add a CashRegister to an empty array', () => {
          const cashRegister: ICashRegister = { id: 123 };
          expectedResult = service.addCashRegisterToCollectionIfMissing([], cashRegister);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cashRegister);
        });

        it('should not add a CashRegister to an array that contains it', () => {
          const cashRegister: ICashRegister = { id: 123 };
          const cashRegisterCollection: ICashRegister[] = [
            {
              ...cashRegister,
            },
            { id: 456 },
          ];
          expectedResult = service.addCashRegisterToCollectionIfMissing(cashRegisterCollection, cashRegister);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CashRegister to an array that doesn't contain it", () => {
          const cashRegister: ICashRegister = { id: 123 };
          const cashRegisterCollection: ICashRegister[] = [{ id: 456 }];
          expectedResult = service.addCashRegisterToCollectionIfMissing(cashRegisterCollection, cashRegister);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cashRegister);
        });

        it('should add only unique CashRegister to an array', () => {
          const cashRegisterArray: ICashRegister[] = [{ id: 123 }, { id: 456 }, { id: 1917 }];
          const cashRegisterCollection: ICashRegister[] = [{ id: 123 }];
          expectedResult = service.addCashRegisterToCollectionIfMissing(cashRegisterCollection, ...cashRegisterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cashRegister: ICashRegister = { id: 123 };
          const cashRegister2: ICashRegister = { id: 456 };
          expectedResult = service.addCashRegisterToCollectionIfMissing([], cashRegister, cashRegister2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cashRegister);
          expect(expectedResult).toContain(cashRegister2);
        });

        it('should accept null and undefined values', () => {
          const cashRegister: ICashRegister = { id: 123 };
          expectedResult = service.addCashRegisterToCollectionIfMissing([], null, cashRegister, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cashRegister);
        });

        it('should return initial array if no CashRegister is added', () => {
          const cashRegisterCollection: ICashRegister[] = [{ id: 123 }];
          expectedResult = service.addCashRegisterToCollectionIfMissing(cashRegisterCollection, undefined, null);
          expect(expectedResult).toEqual(cashRegisterCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
