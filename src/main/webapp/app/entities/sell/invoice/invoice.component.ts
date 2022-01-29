import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot } from '@angular/router';
import { SettingsService } from 'app/entities/settings/service/settings.service';
import { ISettings } from 'app/entities/settings/settings.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { ISell } from '../sell.model';
import { SellService } from '../service/sell.service';
declare let $: any;

@Component({
  selector: 'jhi-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss'],
})
export class InvoiceComponent implements OnInit, OnDestroy {
  sells: ISell[] = [];
  transaction: ITransaction | null = null;
  settings: ISettings | null = null;
  constructor(private settingsService: SettingsService, private sellService: SellService, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.hideSideBar();
    this.settingsService.getCurrentSettings().subscribe(se => {
      this.settings = se;
    });
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      this.loadSells();
    });
  }

  loadSells(): void {
    this.sellService.getSellsByReference(this.transaction?.referenceNo ?? '').subscribe(data => {
      this.sells = data.body ?? [];
    });
  }

  hideSideBar(): void {
    $('body').addClass('closed-sidebar');
    $('#close-sidebar').addClass('hidden');
  }

  showSideBar(): void {
    $('body').removeClass('closed-sidebar');
    $('#close-sidebar').removeClass('hidden');
  }

  printDiv(divId: string): void {
    const printContents = document.getElementById(divId)?.innerHTML;
    const originalContents = document.body.innerHTML;
    if (printContents) {
      document.body.innerHTML = printContents;
      window.print();
      document.body.innerHTML = originalContents;
    }
  }

  previousState(): void {
    window.history.back();
  }

  ngOnDestroy(): void {
    this.showSideBar();
  }
}
