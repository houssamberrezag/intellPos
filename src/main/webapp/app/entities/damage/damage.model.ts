import * as dayjs from 'dayjs';
import { IProduct } from 'app/entities/product/product.model';

export interface IDamage {
  id?: number;
  quantity?: number;
  date?: dayjs.Dayjs | null;
  note?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  product?: IProduct | null;
}

export class Damage implements IDamage {
  constructor(
    public id?: number,
    public quantity?: number,
    public date?: dayjs.Dayjs | null,
    public note?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public product?: IProduct | null
  ) {}
}

export function getDamageIdentifier(damage: IDamage): number | undefined {
  return damage.id;
}
