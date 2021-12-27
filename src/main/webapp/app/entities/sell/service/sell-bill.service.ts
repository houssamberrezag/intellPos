import { Injectable } from '@angular/core';
import { SettingsService } from 'app/entities/settings/service/settings.service';
import { ISettings } from 'app/entities/settings/settings.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import * as dayjs from 'dayjs';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { StyleDictionary, TDocumentDefinitions } from 'pdfmake/interfaces';
import { ISell } from '../sell.model';
import { SellService } from './sell.service';

(<any>pdfMake).vfs = pdfFonts.pdfMake.vfs;
@Injectable({
  providedIn: 'root',
})
export class SellBillService {
  sells: ISell[] = [];
  transaction: ITransaction | null = null;
  settings: ISettings | null = null;
  constructor(private settingsService: SettingsService, private sellService: SellService) {}

  async generatePdf(transaction: ITransaction): Promise<void> {
    this.transaction = transaction;
    const subsc = this.settingsService.getCurrentSettings().subscribe(se => {
      this.settings = se;
    });
    await this.sellService
      .getSellsByReference(transaction.referenceNo ?? '')
      .toPromise()
      .then(res => {
        this.sells = res.body ?? [];
      });
    pdfMake.createPdf(this.getDocumentDefinition()).open();
    subsc.unsubscribe();
  }

  getDocumentDefinition(): TDocumentDefinitions {
    return {
      content: [
        { text: 'FACTURE', style: 'facture' },
        { text: this.settings?.name ?? '', style: 'societeName' },
        {
          columns: [
            { text: 'Tél :', width: 50 },
            { text: this.settings?.phone ?? '', width: 150, alignment: 'right' },
          ],
        },
        {
          columns: [
            { text: 'Adresse :', width: 50 },
            { text: this.settings?.address ?? '', width: 150, alignment: 'right' },
          ],
        },
        {
          columns: [
            { text: 'Client', style: 'subTitle' },
            { text: '' },
            { text: 'Facture No :', style: 'subTitle' },
            { text: this.transaction?.referenceNo ?? '', style: ['invoiceInfo', 'right'] },
          ],
          style: 'info',
        },
        {
          columns: [
            { text: (this.transaction?.person?.firstName ?? '') + ' ' + (this.transaction?.person?.lastName ?? ''), style: '' },

            { text: '' },
            { text: 'Date :', style: 'subTitle' },
            { text: dayjs(this.transaction?.date ?? '').format('DD/MM/YYYY  hh:mm'), style: ['invoiceInfo', 'right'] },
          ],
        },
        {
          columns: [{ text: this.transaction?.person?.phone ?? '', style: '', margin: [0, 0, 0, 30] }],
        },

        {
          layout: 'lightHorizontalLines', // optional
          table: {
            // headers are automatically repeated if the table spans over multiple pages
            // you can declare how many rows should be treated as headers
            headerRows: 1,
            widths: ['*', 50, 100, 150],

            body: [
              //header
              [
                { text: 'Produit', style: ['textCenter'] },
                { text: 'Quantité', style: ['textCenter'] },
                { text: 'Prix unitaire', style: ['textCenter'] },
                { text: 'Sous-total', style: ['textCenter'] },
              ],

              //body
              ...this.sells.map(sell => [
                { text: sell.product?.name, style: ['textCenter'] },
                { text: sell.quantity, style: ['textCenter'] },
                { text: sell.unitPrice?.toFixed(2), style: ['textCenter'] },
                { text: sell.subTotal?.toFixed(2), style: ['textCenter'] },
              ]),
            ],
          },
        },
        { text: '', margin: [0, 10] },
        {
          columns: [
            { text: 'Quantité :', style: ['recap', 'right'], width: '80%' },
            {
              text:
                this.sells
                  .map(sell => sell.quantity ?? 0)
                  .reduce((a, b) => a + b, 0)
                  .toString() + ' articles',
              style: '',
              width: '*',
            },
          ],
        },
        {
          columns: [
            { text: 'total net:', style: ['recap', 'right'], width: '80%' },
            { text: (this.transaction?.netTotal?.toFixed(2) ?? '') + 'dh', style: '', width: '*' },
          ],
        },
        {
          columns: [
            { text: 'payé:', style: ['recap', 'right'], width: '80%' },
            { text: (this.transaction?.paid?.toFixed(2) ?? '') + 'dh', style: '', width: '*' },
          ],
        },
        {
          columns: [
            { text: 'reste', style: ['recap', 'right'], width: '80%' },
            { text: ((this.transaction?.netTotal ?? 0) - (this.transaction?.paid ?? 0)).toFixed(2) + 'dh', style: '', width: '*' },
          ],
        },
      ],
      styles: this.getDocumentStyles(),
    };
  }

  getDocumentStyles(): StyleDictionary {
    return {
      facture: {
        fontSize: 40,
        bold: true,
        color: '#003366',
        margin: [0, 0, 0, 30],
      },
      societeName: {
        italics: true,
        bold: true,
        fontSize: 25,
        margin: [0, 0, 0, 15],
      },
      right: {
        alignment: 'right',
      },
      subTitle: {
        fontSize: 16,
        bold: false,
        color: '#003366',
      },
      info: {
        margin: [0, 25, 0, 5],
      },
      invoiceInfo: {
        margin: [0, 3, 0, 0],
      },
      textCenter: {
        alignment: 'center',
      },
      recap: {
        bold: true,
        margin: [0, 0, 10, 7],
      },
    };
  }
}
