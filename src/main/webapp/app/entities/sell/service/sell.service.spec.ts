import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISell, Sell } from '../sell.model';

import { SellService } from './sell.service';

describe('Service Tests', () => {
  describe('Sell Service', () => {
    let service: SellService;
    let httpMock: HttpTestingController;
    let elemDefault: ISell;
    let expectedResult: ISell | ISell[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SellService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        referenceNo: 'AAAAAAA',
        quantity: 0,
        unitCostPrice: 0,
        subTotal: 0,
        productTax: 0,
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

      it('should create a Sell', () => {
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

        service.create(new Sell()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Sell', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            referenceNo: 'BBBBBB',
            quantity: 1,
            unitCostPrice: 1,
            subTotal: 1,
            productTax: 1,
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

      it('should partial update a Sell', () => {
        const patchObject = Object.assign(
          {
            quantity: 1,
            productTax: 1,
            date: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new Sell()
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

      it('should return a list of Sell', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            referenceNo: 'BBBBBB',
            quantity: 1,
            unitCostPrice: 1,
            subTotal: 1,
            productTax: 1,
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

      it('should delete a Sell', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSellToCollectionIfMissing', () => {
        it('should add a Sell to an empty array', () => {
          const sell: ISell = { id: 123 };
          expectedResult = service.addSellToCollectionIfMissing([], sell);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sell);
        });

        it('should not add a Sell to an array that contains it', () => {
          const sell: ISell = { id: 123 };
          const sellCollection: ISell[] = [
            {
              ...sell,
            },
            { id: 456 },
          ];
          expectedResult = service.addSellToCollectionIfMissing(sellCollection, sell);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Sell to an array that doesn't contain it", () => {
          const sell: ISell = { id: 123 };
          const sellCollection: ISell[] = [{ id: 456 }];
          expectedResult = service.addSellToCollectionIfMissing(sellCollection, sell);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sell);
        });

        it('should add only unique Sell to an array', () => {
          const sellArray: ISell[] = [{ id: 123 }, { id: 456 }, { id: 68990 }];
          const sellCollection: ISell[] = [{ id: 123 }];
          expectedResult = service.addSellToCollectionIfMissing(sellCollection, ...sellArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sell: ISell = { id: 123 };
          const sell2: ISell = { id: 456 };
          expectedResult = service.addSellToCollectionIfMissing([], sell, sell2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sell);
          expect(expectedResult).toContain(sell2);
        });

        it('should accept null and undefined values', () => {
          const sell: ISell = { id: 123 };
          expectedResult = service.addSellToCollectionIfMissing([], null, sell, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sell);
        });

        it('should return initial array if no Sell is added', () => {
          const sellCollection: ISell[] = [{ id: 123 }];
          expectedResult = service.addSellToCollectionIfMissing(sellCollection, undefined, null);
          expect(expectedResult).toEqual(sellCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
