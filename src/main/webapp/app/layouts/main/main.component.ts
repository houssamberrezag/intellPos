import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { Account } from 'app/core/auth/account.model';

import { AccountService } from 'app/core/auth/account.service';
import { SettingsService } from 'app/entities/settings/service/settings.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
})
export class MainComponent implements OnInit {
  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();
  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private settingsService: SettingsService,
    @Inject(DOCUMENT) private document: Document
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        if (account) {
          this.document.body.classList.remove('body-bg-img');
        } else {
          this.document.body.classList.add('body-bg-img');
        }
      });
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.settingsService.oserveCurrentSettings().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'Intellpos';
    }
    this.titleService.setTitle(pageTitle);
  }
}
