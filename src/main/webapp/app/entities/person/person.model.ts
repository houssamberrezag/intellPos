import * as dayjs from 'dayjs';
import { IPayment } from 'app/entities/payment/payment.model';
import { PersonTypes } from 'app/entities/enumerations/person-types.model';

export interface IPerson {
  id?: number;
  firstName?: string;
  lastName?: string | null;
  companyName?: string | null;
  email?: string | null;
  phone?: string | null;
  address?: string | null;
  personType?: PersonTypes;
  proviousDue?: number | null;
  accountNo?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  payments?: IPayment[] | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string | null,
    public companyName?: string | null,
    public email?: string | null,
    public phone?: string | null,
    public address?: string | null,
    public personType?: PersonTypes,
    public proviousDue?: number | null,
    public accountNo?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public payments?: IPayment[] | null
  ) {}
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
