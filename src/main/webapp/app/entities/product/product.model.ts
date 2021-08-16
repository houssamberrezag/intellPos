import * as dayjs from 'dayjs';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { ISubcategorie } from 'app/entities/subcategorie/subcategorie.model';
import { ITaxe } from 'app/entities/taxe/taxe.model';

export interface IProduct {
  id?: number;
  name?: string;
  code?: string;
  quantity?: number | null;
  details?: string | null;
  costPrice?: number;
  minimumRetailPrice?: number | null;
  unit?: string | null;
  status?: boolean | null;
  image?: string | null;
  openingStock?: number | null;
  alertQuantity?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  categorie?: ICategorie;
  subCategorie?: ISubcategorie | null;
  taxe?: ITaxe | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public code?: string,
    public quantity?: number | null,
    public details?: string | null,
    public costPrice?: number,
    public minimumRetailPrice?: number | null,
    public unit?: string | null,
    public status?: boolean | null,
    public image?: string | null,
    public openingStock?: number | null,
    public alertQuantity?: number | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public categorie?: ICategorie,
    public subCategorie?: ISubcategorie | null,
    public taxe?: ITaxe | null
  ) {
    this.status = this.status ?? false;
  }
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
