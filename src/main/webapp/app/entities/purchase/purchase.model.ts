import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IPurchase {
  id?: number;
  referenceNo?: string;
  quantity?: number;
  unitCost?: number;
  subTotal?: number;
  productTax?: number | null;
  date?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  person?: IPerson | null;
  product?: IProduct | null;
}

export class Purchase implements IPurchase {
  constructor(
    public id?: number,
    public referenceNo?: string,
    public quantity?: number,
    public unitCost?: number,
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

export function getPurchaseIdentifier(purchase: IPurchase): number | undefined {
  return purchase.id;
}
