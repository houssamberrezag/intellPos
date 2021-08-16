import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';
import { TransactionTypes } from 'app/entities/enumerations/transaction-types.model';

export interface ITransaction {
  id?: number;
  referenceNo?: string;
  transactionType?: TransactionTypes;
  totalCostPrice?: number | null;
  discount?: number;
  total?: number;
  invoiceTax?: number | null;
  totalTax?: number | null;
  laborCost?: number;
  netTotal?: number | null;
  paid?: number;
  changeAmount?: number | null;
  returnInvoice?: string | null;
  returnBalance?: number | null;
  pos?: boolean;
  date?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  person?: IPerson | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public referenceNo?: string,
    public transactionType?: TransactionTypes,
    public totalCostPrice?: number | null,
    public discount?: number,
    public total?: number,
    public invoiceTax?: number | null,
    public totalTax?: number | null,
    public laborCost?: number,
    public netTotal?: number | null,
    public paid?: number,
    public changeAmount?: number | null,
    public returnInvoice?: string | null,
    public returnBalance?: number | null,
    public pos?: boolean,
    public date?: dayjs.Dayjs | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public person?: IPerson | null
  ) {
    this.pos = this.pos ?? false;
  }
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
