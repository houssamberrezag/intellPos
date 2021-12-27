import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { SettingsService } from 'app/entities/settings/service/settings.service';
import { ISettings } from 'app/entities/settings/settings.model';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SettingsRoutingResolveService implements Resolve<ISettings> {
  constructor(protected service: SettingsService, protected router: Router) {}

  resolve(): Observable<ISettings> | Observable<never> {
    return this.service.getCurrentSettings().pipe(
      mergeMap((settings: ISettings | null) => {
        if (settings) {
          return of(settings);
        } else {
          this.router.navigate(['404']);
          return EMPTY;
        }
      })
    );
  }
}
