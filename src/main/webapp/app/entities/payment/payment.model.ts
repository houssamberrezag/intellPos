import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';
import { PaymentTypes } from 'app/entities/enumerations/payment-types.model';

export interface IPayment {
  id?: number;
  amount?: number;
  method?: string | null;
  type?: PaymentTypes;
  referenceNo?: string | null;
  note?: string | null;
  date?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  person?: IPerson;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public amount?: number,
    public method?: string | null,
    public type?: PaymentTypes,
    public referenceNo?: string | null,
    public note?: string | null,
    public date?: dayjs.Dayjs | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public person?: IPerson
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
