jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TaxeService } from '../service/taxe.service';
import { ITaxe, Taxe } from '../taxe.model';

import { TaxeUpdateComponent } from './taxe-update.component';

describe('Component Tests', () => {
  describe('Taxe Management Update Component', () => {
    let comp: TaxeUpdateComponent;
    let fixture: ComponentFixture<TaxeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let taxeService: TaxeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaxeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TaxeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaxeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      taxeService = TestBed.inject(TaxeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const taxe: ITaxe = { id: 456 };

        activatedRoute.data = of({ taxe });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(taxe));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Taxe>>();
        const taxe = { id: 123 };
        jest.spyOn(taxeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taxe }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(taxeService.update).toHaveBeenCalledWith(taxe);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Taxe>>();
        const taxe = new Taxe();
        jest.spyOn(taxeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taxe }));
        saveSubject.complete();

        // THEN
        expect(taxeService.create).toHaveBeenCalledWith(taxe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Taxe>>();
        const taxe = { id: 123 };
        jest.spyOn(taxeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(taxeService.update).toHaveBeenCalledWith(taxe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
