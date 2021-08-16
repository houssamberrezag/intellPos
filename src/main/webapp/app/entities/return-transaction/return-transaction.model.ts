import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';
import { ISell } from 'app/entities/sell/sell.model';

export interface IReturnTransaction {
  id?: number;
  returnVat?: number;
  sellsReferenceNo?: string;
  returnUnits?: number;
  returnAmount?: number;
  returnedBy?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  person?: IPerson | null;
  sell?: ISell | null;
}

export class ReturnTransaction implements IReturnTransaction {
  constructor(
    public id?: number,
    public returnVat?: number,
    public sellsReferenceNo?: string,
    public returnUnits?: number,
    public returnAmount?: number,
    public returnedBy?: number,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public person?: IPerson | null,
    public sell?: ISell | null
  ) {}
}

export function getReturnTransactionIdentifier(returnTransaction: IReturnTransaction): number | undefined {
  return returnTransaction.id;
}
