export interface ISettings {
  id?: number;
  name?: string;
  slogan?: string;
  address?: string;
  email?: string;
  phone?: string;
}

export class Settings implements ISettings {
  constructor(public id?: number, public name?: string, public slogan?: string, public email?: string, public phone?: string) {}
}

export function getSettingsIdentifier(settings: ISettings): number | undefined {
  return settings.id;
}
