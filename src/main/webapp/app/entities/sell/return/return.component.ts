import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import Swal from 'sweetalert2';
import { ISell, Sell } from '../sell.model';
import { SellService } from '../service/sell.service';

@Component({
  selector: 'jhi-return',
  templateUrl: './return.component.html',
  styleUrls: ['./return.component.scss'],
})
export class ReturnComponent implements OnInit {
  sells: ISell[] | null = [];
  payments: IPayment[] = [];
  transaction: ITransaction | null = null;
  quantityReturnedMap: Map<ISell, number> = new Map<ISell, number>();
  constructor(
    protected activatedRoute: ActivatedRoute,
    private sellService: SellService,
    private alertService: SweetAlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      this.loadSells();
    });
  }

  loadSells(): void {
    this.sellService.getSellsByReference(this.transaction?.referenceNo ?? '').subscribe(data => {
      this.sells = data.body;
      this.initReturnedQuantity();
    });
  }

  initReturnedQuantity(): void {
    this.sells?.forEach(sell => {
      this.quantityReturnedMap.set(sell, 0);
    });
  }

  returnValid(): boolean {
    const quanties = [...this.quantityReturnedMap.values()];

    return !quanties.find(n => n < 0) && quanties.reduce((a, b) => a + b, 0) > 0;
  }

  onQuentityChanged(event: any, sell: ISell): void {
    //console.log(+event.target.value);

    if (+event.target.value) {
      this.quantityReturnedMap.set(sell, +event.target.value);
    } else {
      this.quantityReturnedMap.set(sell, 0);
    }
  }

  save(): void {
    if (this.returnValid()) {
      let continueSave = true;
      this.quantityReturnedMap.forEach((value, key) => {
        if ((key.quantity ?? 0) < value) {
          const warning = `La quantité de retour (${value}) ne peut pas être supérieure à la quantité de vente (${key.quantity ?? 0})`;
          this.alertService.create('', warning, 'error');
          continueSave = false;
        }
      });
      if (continueSave && this.transaction?.id) {
        const keys = [...this.quantityReturnedMap.keys()];
        const dataToSend = keys.map(key => `${key.id ?? 0},${this.quantityReturnedMap.get(key) ?? 0}`);
        this.returnMoneyQuestionAccepted().then(isAccepted => {
          if (isAccepted) {
            this.sellService.returnSell(dataToSend, this.transaction?.id ?? 0).subscribe(res => {
              this.alertService.create('retour efféctué', 'retour efféctué avec succées', 'success');
              this.router.navigate(['/sell']);
            });
          }
        });
      }
    }
  }

  //if difference is greater than due amount then we need to return some money to the customer
  async returnMoneyQuestionAccepted(): Promise<boolean> {
    const due = (this.transaction?.netTotal ?? 0) - (this.transaction?.paid ?? 0);
    const totalPriceOfReturnedItems = [...this.quantityReturnedMap.keys()]
      .map(sell => (sell.unitPrice ?? 0) * (this.quantityReturnedMap.get(sell) ?? 0))
      .reduce((a, b) => a + b, 0);
    if (totalPriceOfReturnedItems > due) {
      const amountToReturn = totalPriceOfReturnedItems - due;
      return (
        await Swal.fire({
          title: `Vous devez rendre ${amountToReturn} Dhs au client`,
          //showDenyButton: true,
          showCancelButton: true,
          confirmButtonText: 'Confirmer',
          cancelButtonText: 'Annuler',
        })
      ).isConfirmed;
    } else {
      return true;
    }
  }
}
