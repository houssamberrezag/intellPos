import * as dayjs from 'dayjs';

export interface ITaxe {
  id?: number;
  name?: string;
  rate?: number;
  type?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export class Taxe implements ITaxe {
  constructor(
    public id?: number,
    public name?: string,
    public rate?: number,
    public type?: number,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null
  ) {}
}

export function getTaxeIdentifier(taxe: ITaxe): number | undefined {
  return taxe.id;
}
