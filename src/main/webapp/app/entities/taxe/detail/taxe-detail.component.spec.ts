import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaxeDetailComponent } from './taxe-detail.component';

describe('Component Tests', () => {
  describe('Taxe Management Detail Component', () => {
    let comp: TaxeDetailComponent;
    let fixture: ComponentFixture<TaxeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TaxeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ taxe: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TaxeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaxeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load taxe on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taxe).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
