import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDamage, Damage } from '../damage.model';
import { DamageService } from '../service/damage.service';

@Injectable({ providedIn: 'root' })
export class DamageRoutingResolveService implements Resolve<IDamage> {
  constructor(protected service: DamageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDamage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((damage: HttpResponse<Damage>) => {
          if (damage.body) {
            return of(damage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Damage());
  }
}
