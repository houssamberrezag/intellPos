import * as dayjs from 'dayjs';
import { ISubcategorie } from 'app/entities/subcategorie/subcategorie.model';

export interface ICategorie {
  id?: number;
  categoryName?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  subcategories?: ISubcategorie[] | null;
}

export class Categorie implements ICategorie {
  constructor(
    public id?: number,
    public categoryName?: string,
    public createdAt?: dayjs.Dayjs | null,
    public updatedAt?: dayjs.Dayjs | null,
    public deletedAt?: dayjs.Dayjs | null,
    public subcategories?: ISubcategorie[] | null
  ) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
