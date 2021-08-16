jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReturnTransactionService } from '../service/return-transaction.service';
import { IReturnTransaction, ReturnTransaction } from '../return-transaction.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ISell } from 'app/entities/sell/sell.model';
import { SellService } from 'app/entities/sell/service/sell.service';

import { ReturnTransactionUpdateComponent } from './return-transaction-update.component';

describe('Component Tests', () => {
  describe('ReturnTransaction Management Update Component', () => {
    let comp: ReturnTransactionUpdateComponent;
    let fixture: ComponentFixture<ReturnTransactionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let returnTransactionService: ReturnTransactionService;
    let personService: PersonService;
    let sellService: SellService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReturnTransactionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReturnTransactionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReturnTransactionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      returnTransactionService = TestBed.inject(ReturnTransactionService);
      personService = TestBed.inject(PersonService);
      sellService = TestBed.inject(SellService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Person query and add missing value', () => {
        const returnTransaction: IReturnTransaction = { id: 456 };
        const person: IPerson = { id: 85513 };
        returnTransaction.person = person;

        const personCollection: IPerson[] = [{ id: 29364 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ returnTransaction });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Sell query and add missing value', () => {
        const returnTransaction: IReturnTransaction = { id: 456 };
        const sell: ISell = { id: 44943 };
        returnTransaction.sell = sell;

        const sellCollection: ISell[] = [{ id: 97959 }];
        jest.spyOn(sellService, 'query').mockReturnValue(of(new HttpResponse({ body: sellCollection })));
        const additionalSells = [sell];
        const expectedCollection: ISell[] = [...additionalSells, ...sellCollection];
        jest.spyOn(sellService, 'addSellToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ returnTransaction });
        comp.ngOnInit();

        expect(sellService.query).toHaveBeenCalled();
        expect(sellService.addSellToCollectionIfMissing).toHaveBeenCalledWith(sellCollection, ...additionalSells);
        expect(comp.sellsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const returnTransaction: IReturnTransaction = { id: 456 };
        const person: IPerson = { id: 47668 };
        returnTransaction.person = person;
        const sell: ISell = { id: 67392 };
        returnTransaction.sell = sell;

        activatedRoute.data = of({ returnTransaction });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(returnTransaction));
        expect(comp.peopleSharedCollection).toContain(person);
        expect(comp.sellsSharedCollection).toContain(sell);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReturnTransaction>>();
        const returnTransaction = { id: 123 };
        jest.spyOn(returnTransactionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnTransaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: returnTransaction }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(returnTransactionService.update).toHaveBeenCalledWith(returnTransaction);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReturnTransaction>>();
        const returnTransaction = new ReturnTransaction();
        jest.spyOn(returnTransactionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnTransaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: returnTransaction }));
        saveSubject.complete();

        // THEN
        expect(returnTransactionService.create).toHaveBeenCalledWith(returnTransaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReturnTransaction>>();
        const returnTransaction = { id: 123 };
        jest.spyOn(returnTransactionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnTransaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(returnTransactionService.update).toHaveBeenCalledWith(returnTransaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSellById', () => {
        it('Should return tracked Sell primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSellById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
