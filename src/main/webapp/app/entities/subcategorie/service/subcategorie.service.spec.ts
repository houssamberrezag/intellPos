import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISubcategorie, Subcategorie } from '../subcategorie.model';

import { SubcategorieService } from './subcategorie.service';

describe('Service Tests', () => {
  describe('Subcategorie Service', () => {
    let service: SubcategorieService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubcategorie;
    let expectedResult: ISubcategorie | ISubcategorie[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SubcategorieService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a Subcategorie', () => {
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

        service.create(new Subcategorie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Subcategorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should partial update a Subcategorie', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            deletedAt: currentDate.format(DATE_TIME_FORMAT),
          },
          new Subcategorie()
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

      it('should return a list of Subcategorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a Subcategorie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSubcategorieToCollectionIfMissing', () => {
        it('should add a Subcategorie to an empty array', () => {
          const subcategorie: ISubcategorie = { id: 123 };
          expectedResult = service.addSubcategorieToCollectionIfMissing([], subcategorie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subcategorie);
        });

        it('should not add a Subcategorie to an array that contains it', () => {
          const subcategorie: ISubcategorie = { id: 123 };
          const subcategorieCollection: ISubcategorie[] = [
            {
              ...subcategorie,
            },
            { id: 456 },
          ];
          expectedResult = service.addSubcategorieToCollectionIfMissing(subcategorieCollection, subcategorie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Subcategorie to an array that doesn't contain it", () => {
          const subcategorie: ISubcategorie = { id: 123 };
          const subcategorieCollection: ISubcategorie[] = [{ id: 456 }];
          expectedResult = service.addSubcategorieToCollectionIfMissing(subcategorieCollection, subcategorie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subcategorie);
        });

        it('should add only unique Subcategorie to an array', () => {
          const subcategorieArray: ISubcategorie[] = [{ id: 123 }, { id: 456 }, { id: 81152 }];
          const subcategorieCollection: ISubcategorie[] = [{ id: 123 }];
          expectedResult = service.addSubcategorieToCollectionIfMissing(subcategorieCollection, ...subcategorieArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const subcategorie: ISubcategorie = { id: 123 };
          const subcategorie2: ISubcategorie = { id: 456 };
          expectedResult = service.addSubcategorieToCollectionIfMissing([], subcategorie, subcategorie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subcategorie);
          expect(expectedResult).toContain(subcategorie2);
        });

        it('should accept null and undefined values', () => {
          const subcategorie: ISubcategorie = { id: 123 };
          expectedResult = service.addSubcategorieToCollectionIfMissing([], null, subcategorie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subcategorie);
        });

        it('should return initial array if no Subcategorie is added', () => {
          const subcategorieCollection: ISubcategorie[] = [{ id: 123 }];
          expectedResult = service.addSubcategorieToCollectionIfMissing(subcategorieCollection, undefined, null);
          expect(expectedResult).toEqual(subcategorieCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
