import * as dayjs from 'dayjs';
import { ICategorie } from 'app/entities/categorie/categorie.model';

export interface ISubcategorie {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  categorie?: ICategorie | null;
}

export class Subcategorie implements ISubcategorie {
  constructor(
    public id?: number,
    public name?: string,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public categorie?: ICategorie | null
  ) {}
}

export function getSubcategorieIdentifier(subcategorie: ISubcategorie): number | undefined {
  return subcategorie.id;
}
