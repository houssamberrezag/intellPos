jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductService } from '../service/product.service';
import { IProduct, Product } from '../product.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { ISubcategorie } from 'app/entities/subcategorie/subcategorie.model';
import { SubcategorieService } from 'app/entities/subcategorie/service/subcategorie.service';
import { ITaxe } from 'app/entities/taxe/taxe.model';
import { TaxeService } from 'app/entities/taxe/service/taxe.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Component Tests', () => {
  describe('Product Management Update Component', () => {
    let comp: ProductUpdateComponent;
    let fixture: ComponentFixture<ProductUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productService: ProductService;
    let categorieService: CategorieService;
    let subcategorieService: SubcategorieService;
    let taxeService: TaxeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productService = TestBed.inject(ProductService);
      categorieService = TestBed.inject(CategorieService);
      subcategorieService = TestBed.inject(SubcategorieService);
      taxeService = TestBed.inject(TaxeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Categorie query and add missing value', () => {
        const product: IProduct = { id: 456 };
        const categorie: ICategorie = { id: 64893 };
        product.categorie = categorie;

        const categorieCollection: ICategorie[] = [{ id: 8125 }];
        jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
        const additionalCategories = [categorie];
        const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
        jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(categorieService.query).toHaveBeenCalled();
        expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(categorieCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Subcategorie query and add missing value', () => {
        const product: IProduct = { id: 456 };
        const subCategorie: ISubcategorie = { id: 19708 };
        product.subCategorie = subCategorie;

        const subcategorieCollection: ISubcategorie[] = [{ id: 59589 }];
        jest.spyOn(subcategorieService, 'query').mockReturnValue(of(new HttpResponse({ body: subcategorieCollection })));
        const additionalSubcategories = [subCategorie];
        const expectedCollection: ISubcategorie[] = [...additionalSubcategories, ...subcategorieCollection];
        jest.spyOn(subcategorieService, 'addSubcategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(subcategorieService.query).toHaveBeenCalled();
        expect(subcategorieService.addSubcategorieToCollectionIfMissing).toHaveBeenCalledWith(
          subcategorieCollection,
          ...additionalSubcategories
        );
        expect(comp.subcategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Taxe query and add missing value', () => {
        const product: IProduct = { id: 456 };
        const taxe: ITaxe = { id: 99044 };
        product.taxe = taxe;

        const taxeCollection: ITaxe[] = [{ id: 89488 }];
        jest.spyOn(taxeService, 'query').mockReturnValue(of(new HttpResponse({ body: taxeCollection })));
        const additionalTaxes = [taxe];
        const expectedCollection: ITaxe[] = [...additionalTaxes, ...taxeCollection];
        jest.spyOn(taxeService, 'addTaxeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(taxeService.query).toHaveBeenCalled();
        expect(taxeService.addTaxeToCollectionIfMissing).toHaveBeenCalledWith(taxeCollection, ...additionalTaxes);
        expect(comp.taxesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const product: IProduct = { id: 456 };
        const categorie: ICategorie = { id: 81992 };
        product.categorie = categorie;
        const subCategorie: ISubcategorie = { id: 5487 };
        product.subCategorie = subCategorie;
        const taxe: ITaxe = { id: 24591 };
        product.taxe = taxe;

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(product));
        expect(comp.categoriesSharedCollection).toContain(categorie);
        expect(comp.subcategoriesSharedCollection).toContain(subCategorie);
        expect(comp.taxesSharedCollection).toContain(taxe);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = { id: 123 };
        jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productService.update).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = new Product();
        jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(productService.create).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = { id: 123 };
        jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productService.update).toHaveBeenCalledWith(product);
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

      describe('trackSubcategorieById', () => {
        it('Should return tracked Subcategorie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSubcategorieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTaxeById', () => {
        it('Should return tracked Taxe primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTaxeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
