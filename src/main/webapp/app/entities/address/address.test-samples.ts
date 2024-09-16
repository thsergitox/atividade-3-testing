import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 17871,
  address1: 'violet ew daintily',
  city: 'Coltworth',
  postcode: 'H',
  country: 'Un',
};

export const sampleWithPartialData: IAddress = {
  id: 27430,
  address1: 'backpack',
  city: 'Flatleyworth',
  postcode: 'r',
  country: 'Ma',
};

export const sampleWithFullData: IAddress = {
  id: 12687,
  address1: 'worthless furthermore',
  address2: 'before next whose',
  city: 'East Nolacester',
  postcode: 'RqVi 0',
  country: 'Ph',
};

export const sampleWithNewData: NewAddress = {
  address1: 'oh credential',
  city: 'Blandaton',
  postcode: 'CU3',
  country: 'Co',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
