import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ISell {
  id?: number;
  referenceNo?: string;
  quantity?: number;
  unitCostPrice?: number | null;
  unitPrice?: number | null;
  subTotal?: number;
  productTax?: number | null;
  date?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  person?: IPerson | null;
  product?: IProduct | null;
}

export class Sell implements ISell {
  constructor(
    public id?: number,
    public referenceNo?: string,
    public quantity?: number,
    public unitCostPrice?: number | null,
    public unitPrice?: number | null,
    public subTotal?: number,
    public productTax?: number | null,
    public date?: dayjs.Dayjs | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public person?: IPerson | null,
    public product?: IProduct | null
  ) {}
}

export function getSellIdentifier(sell: ISell): number | undefined {
  return sell.id;
}
