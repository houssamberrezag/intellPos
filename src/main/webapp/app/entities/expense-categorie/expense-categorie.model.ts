import * as dayjs from 'dayjs';
import { IExpense } from 'app/entities/expense/expense.model';

export interface IExpenseCategorie {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  expenses?: IExpense[] | null;
}

export class ExpenseCategorie implements IExpenseCategorie {
  constructor(
    public id?: number,
    public name?: string,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public expenses?: IExpense[] | null
  ) {}
}

export function getExpenseCategorieIdentifier(expenseCategorie: IExpenseCategorie): number | undefined {
  return expenseCategorie.id;
}
