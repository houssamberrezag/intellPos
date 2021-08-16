import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubcategorieDetailComponent } from './subcategorie-detail.component';

describe('Component Tests', () => {
  describe('Subcategorie Management Detail Component', () => {
    let comp: SubcategorieDetailComponent;
    let fixture: ComponentFixture<SubcategorieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SubcategorieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ subcategorie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SubcategorieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubcategorieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load subcategorie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subcategorie).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
