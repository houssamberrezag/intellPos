jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExpenseService } from '../service/expense.service';
import { IExpense, Expense } from '../expense.model';
import { IExpenseCategorie } from 'app/entities/expense-categorie/expense-categorie.model';
import { ExpenseCategorieService } from 'app/entities/expense-categorie/service/expense-categorie.service';

import { ExpenseUpdateComponent } from './expense-update.component';

describe('Component Tests', () => {
  describe('Expense Management Update Component', () => {
    let comp: ExpenseUpdateComponent;
    let fixture: ComponentFixture<ExpenseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let expenseService: ExpenseService;
    let expenseCategorieService: ExpenseCategorieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpenseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExpenseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      expenseService = TestBed.inject(ExpenseService);
      expenseCategorieService = TestBed.inject(ExpenseCategorieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ExpenseCategorie query and add missing value', () => {
        const expense: IExpense = { id: 456 };
        const expenseCategorie: IExpenseCategorie = { id: 43748 };
        expense.expenseCategorie = expenseCategorie;

        const expenseCategorieCollection: IExpenseCategorie[] = [{ id: 46765 }];
        jest.spyOn(expenseCategorieService, 'query').mockReturnValue(of(new HttpResponse({ body: expenseCategorieCollection })));
        const additionalExpenseCategories = [expenseCategorie];
        const expectedCollection: IExpenseCategorie[] = [...additionalExpenseCategories, ...expenseCategorieCollection];
        jest.spyOn(expenseCategorieService, 'addExpenseCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ expense });
        comp.ngOnInit();

        expect(expenseCategorieService.query).toHaveBeenCalled();
        expect(expenseCategorieService.addExpenseCategorieToCollectionIfMissing).toHaveBeenCalledWith(
          expenseCategorieCollection,
          ...additionalExpenseCategories
        );
        expect(comp.expenseCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const expense: IExpense = { id: 456 };
        const expenseCategorie: IExpenseCategorie = { id: 34586 };
        expense.expenseCategorie = expenseCategorie;

        activatedRoute.data = of({ expense });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(expense));
        expect(comp.expenseCategoriesSharedCollection).toContain(expenseCategorie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Expense>>();
        const expense = { id: 123 };
        jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expense });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expense }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(expenseService.update).toHaveBeenCalledWith(expense);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Expense>>();
        const expense = new Expense();
        jest.spyOn(expenseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expense });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expense }));
        saveSubject.complete();

        // THEN
        expect(expenseService.create).toHaveBeenCalledWith(expense);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Expense>>();
        const expense = { id: 123 };
        jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expense });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(expenseService.update).toHaveBeenCalledWith(expense);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackExpenseCategorieById', () => {
        it('Should return tracked ExpenseCategorie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackExpenseCategorieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
