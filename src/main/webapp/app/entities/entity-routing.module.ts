import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'return-transaction',
        data: { pageTitle: 'ReturnTransactions' },
        loadChildren: () => import('./return-transaction/return-transaction.module').then(m => m.ReturnTransactionModule),
      },
      {
        path: 'sell',
        data: { pageTitle: 'Sells' },
        loadChildren: () => import('./sell/sell.module').then(m => m.SellModule),
      },
      {
        path: 'taxe',
        data: { pageTitle: 'Taxes' },
        loadChildren: () => import('./taxe/taxe.module').then(m => m.TaxeModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'Transactions' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      {
        path: 'cash-register',
        data: { pageTitle: 'CashRegisters' },
        loadChildren: () => import('./cash-register/cash-register.module').then(m => m.CashRegisterModule),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CategorieModule),
      },
      {
        path: 'subcategorie',
        data: { pageTitle: 'Subcategories' },
        loadChildren: () => import('./subcategorie/subcategorie.module').then(m => m.SubcategorieModule),
      },
      {
        path: 'person',
        data: { pageTitle: 'People' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'damage',
        data: { pageTitle: 'Damages' },
        loadChildren: () => import('./damage/damage.module').then(m => m.DamageModule),
      },
      {
        path: 'expense-categorie',
        data: { pageTitle: 'ExpenseCategories' },
        loadChildren: () => import('./expense-categorie/expense-categorie.module').then(m => m.ExpenseCategorieModule),
      },
      {
        path: 'expense',
        data: { pageTitle: 'Expenses' },
        loadChildren: () => import('./expense/expense.module').then(m => m.ExpenseModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'Payments' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'Products' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'purchase',
        data: { pageTitle: 'Purchases' },
        loadChildren: () => import('./purchase/purchase.module').then(m => m.PurchaseModule),
      },
      {
        path: 'report',
        data: { pageTitle: 'Report' },
        loadChildren: () => import('./report/report.module').then(m => m.ReportModule),
      },
      {
        path: 'settings',
        data: { pageTitle: 'Settings' },
        loadChildren: () => import('./settings/settings.module').then(m => m.SettingsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
