import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SellDetailComponent } from './sell-detail.component';

describe('Component Tests', () => {
  describe('Sell Management Detail Component', () => {
    let comp: SellDetailComponent;
    let fixture: ComponentFixture<SellDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SellDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sell: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SellDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SellDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sell on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sell).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
