import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 5964,
  firstName: 'Karlie',
  lastName: "O'Hara",
  email: 'HQ3@gL.g.-GQD.5ID',
};

export const sampleWithPartialData: ICustomer = {
  id: 19427,
  firstName: 'Marina',
  lastName: 'Kilback',
  email: 'O@vWDTo.NeuC',
  telephone: '371234',
};

export const sampleWithFullData: ICustomer = {
  id: 8139,
  firstName: 'Kiera',
  lastName: 'Huels',
  email: '._eW@DDFlHp.CNFf',
  telephone: '923',
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Helmer',
  lastName: 'Bechtelar',
  email: 'ZALkV@5E3R_N.B-vrLs.Tm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
