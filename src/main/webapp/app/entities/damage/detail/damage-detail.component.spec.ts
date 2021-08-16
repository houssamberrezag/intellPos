import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DamageDetailComponent } from './damage-detail.component';

describe('Component Tests', () => {
  describe('Damage Management Detail Component', () => {
    let comp: DamageDetailComponent;
    let fixture: ComponentFixture<DamageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DamageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ damage: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DamageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DamageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load damage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.damage).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
