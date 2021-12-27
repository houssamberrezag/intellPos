import { Injectable } from '@angular/core';
import { PaymentTypes } from 'app/entities/enumerations/payment-types.model';
import { SettingsService } from 'app/entities/settings/service/settings.service';
import { ISettings } from 'app/entities/settings/settings.model';
import * as dayjs from 'dayjs';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { StyleDictionary, TDocumentDefinitions } from 'pdfmake/interfaces';
import { IPayment } from '../payment.model';
(<any>pdfMake).vfs = pdfFonts.pdfMake.vfs;

@Injectable({
  providedIn: 'root',
})
export class PaymentBillService {
  payment: IPayment | null = null;
  settings: ISettings | null = null;
  constructor(private settingsService: SettingsService) {}

  generatePdf(payment: IPayment): void {
    this.payment = payment;
    this.settingsService
      .getCurrentSettings()
      .subscribe(se => {
        this.settings = se;
        pdfMake.createPdf(this.getDocumentDefinition()).open();
      })
      .unsubscribe();
  }

  getDocumentDefinition(): TDocumentDefinitions {
    return {
      content: [
        { text: this.payment?.type === PaymentTypes.CREDIT ? 'RECEVOIR' : 'PAIEMENT', style: 'facture' },
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
            { text: this.payment?.type === PaymentTypes.CREDIT ? 'Client' : 'Fournisseur', style: 'subTitle' },
            { text: '' },
            { text: 'Facture No :', style: 'subTitle' },
            { text: this.payment?.referenceNo ?? '-', style: ['invoiceInfo', 'right'] },
          ],
          style: 'info',
        },
        {
          columns: [
            { text: (this.payment?.person?.firstName ?? '') + ' ' + (this.payment?.person?.lastName ?? ''), style: '' },

            { text: '' },
            { text: 'Date :', style: 'subTitle' },
            { text: dayjs(this.payment?.date ?? '').format('DD/MM/YYYY  hh:mm'), style: ['invoiceInfo', 'right'] },
          ],
        },
        {
          columns: [{ text: this.payment?.person?.phone ?? '', style: '', margin: [0, 0, 0, 30] }],
        },

        {
          //layout: 'lightHorizontalLines', // optional
          table: {
            // headers are automatically repeated if the table spans over multiple pages
            // you can declare how many rows should be treated as headers
            headerRows: 1,
            widths: ['55%', '45%'],

            body: [
              //header
              [
                { text: 'Note', style: ['textCenter'] },
                { text: 'Montant', style: ['textCenter'] },
              ],

              //body

              [
                { text: this.payment?.note, style: ['textCenter'], margin: [0, 20] },
                { text: (this.payment?.amount?.toFixed(2) ?? '') + ' dh', style: ['textCenter'], margin: [0, 20] },
              ],
            ],
          },
        },
        { text: '', margin: [0, 10] },
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
