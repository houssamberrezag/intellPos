import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TransactionTypes } from 'app/entities/enumerations/transaction-types.model';
import { ITransaction, Transaction } from '../transaction.model';

import { TransactionService } from './transaction.service';

describe('Service Tests', () => {
  describe('Transaction Service', () => {
    let service: TransactionService;
    let httpMock: HttpTestingController;
    let elemDefault: ITransaction;
    let expectedResult: ITransaction | ITransaction[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TransactionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        referenceNo: 'AAAAAAA',
        transactionType: TransactionTypes.OPENING,
        totalCostPrice: 0,
        discount: 0,
        total: 0,
        invoiceTax: 0,
        totalTax: 0,
        laborCost: 0,
        netTotal: 0,
        paid: 0,
        changeAmount: 0,
        returnInvoice: 'AAAAAAA',
        returnBalance: 0,
        pos: false,
        date: currentDate,
        createdAt: currentDate,
        updatedAt: currentDate,
        deletedAt: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
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

      it('should create a Transaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            createdAt: currentDate,
            updatedAt: currentDate,
            deletedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new Transaction()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Transaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            referenceNo: 'BBBBBB',
            transactionType: 'BBBBBB',
            totalCostPrice: 1,
            discount: 1,
            total: 1,
            invoiceTax: 1,
            totalTax: 1,
            laborCost: 1,
            netTotal: 1,
            paid: 1,
            changeAmount: 1,
            returnInvoice: 'BBBBBB',
            returnBalance: 1,
            pos: true,
            date: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
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

      it('should partial update a Transaction', () => {
        const patchObject = Object.assign(
          {
            referenceNo: 'BBBBBB',
            totalCostPrice: 1,
            discount: 1,
            total: 1,
            invoiceTax: 1,
            totalTax: 1,
            laborCost: 1,
            paid: 1,
            returnBalance: 1,
            pos: true,
            date: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new Transaction()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
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

      it('should return a list of Transaction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            referenceNo: 'BBBBBB',
            transactionType: 'BBBBBB',
            totalCostPrice: 1,
            discount: 1,
            total: 1,
            invoiceTax: 1,
            totalTax: 1,
            laborCost: 1,
            netTotal: 1,
            paid: 1,
            changeAmount: 1,
            returnInvoice: 'BBBBBB',
            returnBalance: 1,
            pos: true,
            date: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
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

      it('should delete a Transaction', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTransactionToCollectionIfMissing', () => {
        it('should add a Transaction to an empty array', () => {
          const transaction: ITransaction = { id: 123 };
          expectedResult = service.addTransactionToCollectionIfMissing([], transaction);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(transaction);
        });

        it('should not add a Transaction to an array that contains it', () => {
          const transaction: ITransaction = { id: 123 };
          const transactionCollection: ITransaction[] = [
            {
              ...transaction,
            },
            { id: 456 },
          ];
          expectedResult = service.addTransactionToCollectionIfMissing(transactionCollection, transaction);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Transaction to an array that doesn't contain it", () => {
          const transaction: ITransaction = { id: 123 };
          const transactionCollection: ITransaction[] = [{ id: 456 }];
          expectedResult = service.addTransactionToCollectionIfMissing(transactionCollection, transaction);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(transaction);
        });

        it('should add only unique Transaction to an array', () => {
          const transactionArray: ITransaction[] = [{ id: 123 }, { id: 456 }, { id: 10086 }];
          const transactionCollection: ITransaction[] = [{ id: 123 }];
          expectedResult = service.addTransactionToCollectionIfMissing(transactionCollection, ...transactionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const transaction: ITransaction = { id: 123 };
          const transaction2: ITransaction = { id: 456 };
          expectedResult = service.addTransactionToCollectionIfMissing([], transaction, transaction2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(transaction);
          expect(expectedResult).toContain(transaction2);
        });

        it('should accept null and undefined values', () => {
          const transaction: ITransaction = { id: 123 };
          expectedResult = service.addTransactionToCollectionIfMissing([], null, transaction, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(transaction);
        });

        it('should return initial array if no Transaction is added', () => {
          const transactionCollection: ITransaction[] = [{ id: 123 }];
          expectedResult = service.addTransactionToCollectionIfMissing(transactionCollection, undefined, null);
          expect(expectedResult).toEqual(transactionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
