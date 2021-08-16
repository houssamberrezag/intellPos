import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpenseDetailComponent } from './expense-detail.component';

describe('Component Tests', () => {
  describe('Expense Management Detail Component', () => {
    let comp: ExpenseDetailComponent;
    let fixture: ComponentFixture<ExpenseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExpenseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ expense: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ExpenseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpenseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load expense on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.expense).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
