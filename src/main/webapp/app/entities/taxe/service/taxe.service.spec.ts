import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITaxe, Taxe } from '../taxe.model';

import { TaxeService } from './taxe.service';

describe('Service Tests', () => {
  describe('Taxe Service', () => {
    let service: TaxeService;
    let httpMock: HttpTestingController;
    let elemDefault: ITaxe;
    let expectedResult: ITaxe | ITaxe[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TaxeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        rate: 0,
        type: 0,
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

      it('should create a Taxe', () => {
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

        service.create(new Taxe()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Taxe', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            rate: 1,
            type: 1,
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

      it('should partial update a Taxe', () => {
        const patchObject = Object.assign(
          {
            type: 1,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new Taxe()
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

      it('should return a list of Taxe', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            rate: 1,
            type: 1,
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

      it('should delete a Taxe', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTaxeToCollectionIfMissing', () => {
        it('should add a Taxe to an empty array', () => {
          const taxe: ITaxe = { id: 123 };
          expectedResult = service.addTaxeToCollectionIfMissing([], taxe);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(taxe);
        });

        it('should not add a Taxe to an array that contains it', () => {
          const taxe: ITaxe = { id: 123 };
          const taxeCollection: ITaxe[] = [
            {
              ...taxe,
            },
            { id: 456 },
          ];
          expectedResult = service.addTaxeToCollectionIfMissing(taxeCollection, taxe);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Taxe to an array that doesn't contain it", () => {
          const taxe: ITaxe = { id: 123 };
          const taxeCollection: ITaxe[] = [{ id: 456 }];
          expectedResult = service.addTaxeToCollectionIfMissing(taxeCollection, taxe);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(taxe);
        });

        it('should add only unique Taxe to an array', () => {
          const taxeArray: ITaxe[] = [{ id: 123 }, { id: 456 }, { id: 39884 }];
          const taxeCollection: ITaxe[] = [{ id: 123 }];
          expectedResult = service.addTaxeToCollectionIfMissing(taxeCollection, ...taxeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const taxe: ITaxe = { id: 123 };
          const taxe2: ITaxe = { id: 456 };
          expectedResult = service.addTaxeToCollectionIfMissing([], taxe, taxe2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(taxe);
          expect(expectedResult).toContain(taxe2);
        });

        it('should accept null and undefined values', () => {
          const taxe: ITaxe = { id: 123 };
          expectedResult = service.addTaxeToCollectionIfMissing([], null, taxe, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(taxe);
        });

        it('should return initial array if no Taxe is added', () => {
          const taxeCollection: ITaxe[] = [{ id: 123 }];
          expectedResult = service.addTaxeToCollectionIfMissing(taxeCollection, undefined, null);
          expect(expectedResult).toEqual(taxeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
