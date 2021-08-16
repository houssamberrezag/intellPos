import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CashRegisterDetailComponent } from './cash-register-detail.component';

describe('Component Tests', () => {
  describe('CashRegister Management Detail Component', () => {
    let comp: CashRegisterDetailComponent;
    let fixture: ComponentFixture<CashRegisterDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CashRegisterDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cashRegister: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CashRegisterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashRegisterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cashRegister on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cashRegister).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
