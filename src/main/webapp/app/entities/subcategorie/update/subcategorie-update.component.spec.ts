jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SubcategorieService } from '../service/subcategorie.service';
import { ISubcategorie, Subcategorie } from '../subcategorie.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

import { SubcategorieUpdateComponent } from './subcategorie-update.component';

describe('Component Tests', () => {
  describe('Subcategorie Management Update Component', () => {
    let comp: SubcategorieUpdateComponent;
    let fixture: ComponentFixture<SubcategorieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let subcategorieService: SubcategorieService;
    let categorieService: CategorieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SubcategorieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SubcategorieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubcategorieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      subcategorieService = TestBed.inject(SubcategorieService);
      categorieService = TestBed.inject(CategorieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Categorie query and add missing value', () => {
        const subcategorie: ISubcategorie = { id: 456 };
        const categorie: ICategorie = { id: 94613 };
        subcategorie.categorie = categorie;

        const categorieCollection: ICategorie[] = [{ id: 20164 }];
        jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
        const additionalCategories = [categorie];
        const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
        jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ subcategorie });
        comp.ngOnInit();

        expect(categorieService.query).toHaveBeenCalled();
        expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(categorieCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const subcategorie: ISubcategorie = { id: 456 };
        const categorie: ICategorie = { id: 71286 };
        subcategorie.categorie = categorie;

        activatedRoute.data = of({ subcategorie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(subcategorie));
        expect(comp.categoriesSharedCollection).toContain(categorie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Subcategorie>>();
        const subcategorie = { id: 123 };
        jest.spyOn(subcategorieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ subcategorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subcategorie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(subcategorieService.update).toHaveBeenCalledWith(subcategorie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Subcategorie>>();
        const subcategorie = new Subcategorie();
        jest.spyOn(subcategorieService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ subcategorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subcategorie }));
        saveSubject.complete();

        // THEN
        expect(subcategorieService.create).toHaveBeenCalledWith(subcategorie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Subcategorie>>();
        const subcategorie = { id: 123 };
        jest.spyOn(subcategorieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ subcategorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(subcategorieService.update).toHaveBeenCalledWith(subcategorie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCategorieById', () => {
        it('Should return tracked Categorie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategorieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
