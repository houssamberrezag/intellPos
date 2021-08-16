import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDamage, Damage } from '../damage.model';

import { DamageService } from './damage.service';

describe('Service Tests', () => {
  describe('Damage Service', () => {
    let service: DamageService;
    let httpMock: HttpTestingController;
    let elemDefault: IDamage;
    let expectedResult: IDamage | IDamage[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DamageService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        quantity: 0,
        date: currentDate,
        note: 'AAAAAAA',
        createdAt: currentDate,
        updatedAt: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
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

      it('should create a Damage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
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

        service.create(new Damage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Damage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantity: 1,
            date: currentDate.format(DATE_TIME_FORMAT),
            note: 'BBBBBB',
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

      it('should partial update a Damage', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new Damage()
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

      it('should return a list of Damage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantity: 1,
            date: currentDate.format(DATE_TIME_FORMAT),
            note: 'BBBBBB',
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

      it('should delete a Damage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDamageToCollectionIfMissing', () => {
        it('should add a Damage to an empty array', () => {
          const damage: IDamage = { id: 123 };
          expectedResult = service.addDamageToCollectionIfMissing([], damage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(damage);
        });

        it('should not add a Damage to an array that contains it', () => {
          const damage: IDamage = { id: 123 };
          const damageCollection: IDamage[] = [
            {
              ...damage,
            },
            { id: 456 },
          ];
          expectedResult = service.addDamageToCollectionIfMissing(damageCollection, damage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Damage to an array that doesn't contain it", () => {
          const damage: IDamage = { id: 123 };
          const damageCollection: IDamage[] = [{ id: 456 }];
          expectedResult = service.addDamageToCollectionIfMissing(damageCollection, damage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(damage);
        });

        it('should add only unique Damage to an array', () => {
          const damageArray: IDamage[] = [{ id: 123 }, { id: 456 }, { id: 38976 }];
          const damageCollection: IDamage[] = [{ id: 123 }];
          expectedResult = service.addDamageToCollectionIfMissing(damageCollection, ...damageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const damage: IDamage = { id: 123 };
          const damage2: IDamage = { id: 456 };
          expectedResult = service.addDamageToCollectionIfMissing([], damage, damage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(damage);
          expect(expectedResult).toContain(damage2);
        });

        it('should accept null and undefined values', () => {
          const damage: IDamage = { id: 123 };
          expectedResult = service.addDamageToCollectionIfMissing([], null, damage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(damage);
        });

        it('should return initial array if no Damage is added', () => {
          const damageCollection: IDamage[] = [{ id: 123 }];
          expectedResult = service.addDamageToCollectionIfMissing(damageCollection, undefined, null);
          expect(expectedResult).toEqual(damageCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
