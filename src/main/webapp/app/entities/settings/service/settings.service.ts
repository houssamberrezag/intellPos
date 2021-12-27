import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable, of, ReplaySubject } from 'rxjs';
import { shareReplay, tap, catchError } from 'rxjs/operators';
import { getSettingsIdentifier, ISettings } from '../settings.model';

export type EntityResponseType = HttpResponse<ISettings>;

@Injectable({
  providedIn: 'root',
})
export class SettingsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/settings');
  private currentSettings = new ReplaySubject<ISettings | null>(1);

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(settings: ISettings): Observable<EntityResponseType> {
    return this.http.post<ISettings>(this.resourceUrl, settings, { observe: 'response' });
  }

  update(settings: ISettings): Observable<EntityResponseType> {
    return this.http.put<ISettings>(`${this.resourceUrl}/${getSettingsIdentifier(settings) as number}`, settings, { observe: 'response' });
  }

  getCurrentSettings(): Observable<ISettings | null> {
    return this.currentSettings.asObservable();
  }

  shareNewSettings(settings: ISettings | null): void {
    this.currentSettings.next(settings);
  }

  oserveCurrentSettings(): Observable<ISettings | null> {
    return this.findCurrentSettings().pipe(
      catchError(() => of(null)),
      tap((settings: ISettings | null) => {
        this.shareNewSettings(settings);
      }),
      shareReplay()
    );
  }

  private findCurrentSettings(): Observable<ISettings> {
    return this.http.get<ISettings>(this.resourceUrl + '/current', { observe: 'body' });
  }
}
