import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '8b9bb155-a1c7-4bdf-ab2b-3df83514dfef',
};

export const sampleWithPartialData: IAuthority = {
  name: '731e03d5-db35-4f18-849b-3e29e828ec75',
};

export const sampleWithFullData: IAuthority = {
  name: '847aaba4-d7eb-466a-a1e3-1a06577e6bd3',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
