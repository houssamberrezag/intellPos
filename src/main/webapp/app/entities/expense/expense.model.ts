import * as dayjs from 'dayjs';
import { IExpenseCategorie } from 'app/entities/expense-categorie/expense-categorie.model';

export interface IExpense {
  id?: number;
  purpose?: string | null;
  amount?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  expenseCategorie?: IExpenseCategorie | null;
}

export class Expense implements IExpense {
  constructor(
    public id?: number,
    public purpose?: string | null,
    public amount?: number,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public expenseCategorie?: IExpenseCategorie | null
  ) {}
}

export function getExpenseIdentifier(expense: IExpense): number | undefined {
  return expense.id;
}
