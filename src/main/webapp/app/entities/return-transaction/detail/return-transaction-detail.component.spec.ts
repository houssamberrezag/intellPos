import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReturnTransactionDetailComponent } from './return-transaction-detail.component';

describe('Component Tests', () => {
  describe('ReturnTransaction Management Detail Component', () => {
    let comp: ReturnTransactionDetailComponent;
    let fixture: ComponentFixture<ReturnTransactionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ReturnTransactionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ returnTransaction: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ReturnTransactionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReturnTransactionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load returnTransaction on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.returnTransaction).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
