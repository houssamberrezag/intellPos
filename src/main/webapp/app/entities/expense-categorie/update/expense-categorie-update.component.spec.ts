jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExpenseCategorieService } from '../service/expense-categorie.service';
import { IExpenseCategorie, ExpenseCategorie } from '../expense-categorie.model';

import { ExpenseCategorieUpdateComponent } from './expense-categorie-update.component';

describe('Component Tests', () => {
  describe('ExpenseCategorie Management Update Component', () => {
    let comp: ExpenseCategorieUpdateComponent;
    let fixture: ComponentFixture<ExpenseCategorieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let expenseCategorieService: ExpenseCategorieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpenseCategorieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExpenseCategorieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseCategorieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      expenseCategorieService = TestBed.inject(ExpenseCategorieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const expenseCategorie: IExpenseCategorie = { id: 456 };

        activatedRoute.data = of({ expenseCategorie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(expenseCategorie));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExpenseCategorie>>();
        const expenseCategorie = { id: 123 };
        jest.spyOn(expenseCategorieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expenseCategorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expenseCategorie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(expenseCategorieService.update).toHaveBeenCalledWith(expenseCategorie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExpenseCategorie>>();
        const expenseCategorie = new ExpenseCategorie();
        jest.spyOn(expenseCategorieService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expenseCategorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expenseCategorie }));
        saveSubject.complete();

        // THEN
        expect(expenseCategorieService.create).toHaveBeenCalledWith(expenseCategorie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExpenseCategorie>>();
        const expenseCategorie = { id: 123 };
        jest.spyOn(expenseCategorieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expenseCategorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(expenseCategorieService.update).toHaveBeenCalledWith(expenseCategorie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
