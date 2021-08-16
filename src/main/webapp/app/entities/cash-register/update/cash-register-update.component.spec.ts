jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CashRegisterService } from '../service/cash-register.service';
import { ICashRegister, CashRegister } from '../cash-register.model';

import { CashRegisterUpdateComponent } from './cash-register-update.component';

describe('Component Tests', () => {
  describe('CashRegister Management Update Component', () => {
    let comp: CashRegisterUpdateComponent;
    let fixture: ComponentFixture<CashRegisterUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cashRegisterService: CashRegisterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CashRegisterUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CashRegisterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashRegisterUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cashRegisterService = TestBed.inject(CashRegisterService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const cashRegister: ICashRegister = { id: 456 };

        activatedRoute.data = of({ cashRegister });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cashRegister));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CashRegister>>();
        const cashRegister = { id: 123 };
        jest.spyOn(cashRegisterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cashRegister });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cashRegister }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cashRegisterService.update).toHaveBeenCalledWith(cashRegister);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CashRegister>>();
        const cashRegister = new CashRegister();
        jest.spyOn(cashRegisterService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cashRegister });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cashRegister }));
        saveSubject.complete();

        // THEN
        expect(cashRegisterService.create).toHaveBeenCalledWith(cashRegister);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CashRegister>>();
        const cashRegister = { id: 123 };
        jest.spyOn(cashRegisterService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cashRegister });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cashRegisterService.update).toHaveBeenCalledWith(cashRegister);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
