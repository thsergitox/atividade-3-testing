import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 3716,
  login: 'Pdt@-T\\8mlUv\\[DJfx7v\\[qihrej\\yLlDuwW',
};

export const sampleWithPartialData: IUser = {
  id: 24059,
  login: 'Yx.',
};

export const sampleWithFullData: IUser = {
  id: 30191,
  login: 'of@R\\uJevL',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
