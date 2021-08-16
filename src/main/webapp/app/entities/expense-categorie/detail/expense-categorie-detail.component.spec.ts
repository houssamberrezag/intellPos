import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpenseCategorieDetailComponent } from './expense-categorie-detail.component';

describe('Component Tests', () => {
  describe('ExpenseCategorie Management Detail Component', () => {
    let comp: ExpenseCategorieDetailComponent;
    let fixture: ComponentFixture<ExpenseCategorieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExpenseCategorieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ expenseCategorie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ExpenseCategorieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpenseCategorieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load expenseCategorie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.expenseCategorie).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
