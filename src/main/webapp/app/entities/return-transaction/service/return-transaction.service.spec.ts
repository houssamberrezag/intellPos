import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReturnTransaction, ReturnTransaction } from '../return-transaction.model';

import { ReturnTransactionService } from './return-transaction.service';

describe('Service Tests', () => {
  describe('ReturnTransaction Service', () => {
    let service: ReturnTransactionService;
    let httpMock: HttpTestingController;
    let elemDefault: IReturnTransaction;
    let expectedResult: IReturnTransaction | IReturnTransaction[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReturnTransactionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        returnVat: 0,
        sellsReferenceNo: 'AAAAAAA',
        returnUnits: 0,
        returnAmount: 0,
        returnedBy: 0,
        createdAt: currentDate,
        updatedAt: currentDate,
        deletedAt: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ReturnTransaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
            deletedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new ReturnTransaction()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ReturnTransaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            returnVat: 1,
            sellsReferenceNo: 'BBBBBB',
            returnUnits: 1,
            returnAmount: 1,
            returnedBy: 1,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
            deletedAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ReturnTransaction', () => {
        const patchObject = Object.assign(
          {
            returnVat: 1,
            sellsReferenceNo: 'BBBBBB',
            returnUnits: 1,
            returnedBy: 1,
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new ReturnTransaction()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
            deletedAt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ReturnTransaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            returnVat: 1,
            sellsReferenceNo: 'BBBBBB',
            returnUnits: 1,
            returnAmount: 1,
            returnedBy: 1,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
            deletedAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ReturnTransaction', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReturnTransactionToCollectionIfMissing', () => {
        it('should add a ReturnTransaction to an empty array', () => {
          const returnTransaction: IReturnTransaction = { id: 123 };
          expectedResult = service.addReturnTransactionToCollectionIfMissing([], returnTransaction);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(returnTransaction);
        });

        it('should not add a ReturnTransaction to an array that contains it', () => {
          const returnTransaction: IReturnTransaction = { id: 123 };
          const returnTransactionCollection: IReturnTransaction[] = [
            {
              ...returnTransaction,
            },
            { id: 456 },
          ];
          expectedResult = service.addReturnTransactionToCollectionIfMissing(returnTransactionCollection, returnTransaction);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ReturnTransaction to an array that doesn't contain it", () => {
          const returnTransaction: IReturnTransaction = { id: 123 };
          const returnTransactionCollection: IReturnTransaction[] = [{ id: 456 }];
          expectedResult = service.addReturnTransactionToCollectionIfMissing(returnTransactionCollection, returnTransaction);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(returnTransaction);
        });

        it('should add only unique ReturnTransaction to an array', () => {
          const returnTransactionArray: IReturnTransaction[] = [{ id: 123 }, { id: 456 }, { id: 92594 }];
          const returnTransactionCollection: IReturnTransaction[] = [{ id: 123 }];
          expectedResult = service.addReturnTransactionToCollectionIfMissing(returnTransactionCollection, ...returnTransactionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const returnTransaction: IReturnTransaction = { id: 123 };
          const returnTransaction2: IReturnTransaction = { id: 456 };
          expectedResult = service.addReturnTransactionToCollectionIfMissing([], returnTransaction, returnTransaction2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(returnTransaction);
          expect(expectedResult).toContain(returnTransaction2);
        });

        it('should accept null and undefined values', () => {
          const returnTransaction: IReturnTransaction = { id: 123 };
          expectedResult = service.addReturnTransactionToCollectionIfMissing([], null, returnTransaction, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(returnTransaction);
        });

        it('should return initial array if no ReturnTransaction is added', () => {
          const returnTransactionCollection: IReturnTransaction[] = [{ id: 123 }];
          expectedResult = service.addReturnTransactionToCollectionIfMissing(returnTransactionCollection, undefined, null);
          expect(expectedResult).toEqual(returnTransactionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
