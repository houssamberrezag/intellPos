import * as dayjs from 'dayjs';

export interface ICashRegister {
  id?: number;
  cashInHands?: number;
  date?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export class CashRegister implements ICashRegister {
  constructor(
    public id?: number,
    public cashInHands?: number,
    public date?: dayjs.Dayjs,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null
  ) {}
}

export function getCashRegisterIdentifier(cashRegister: ICashRegister): number | undefined {
  return cashRegister.id;
}
