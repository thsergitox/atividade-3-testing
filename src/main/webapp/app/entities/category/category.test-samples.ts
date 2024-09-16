import dayjs from 'dayjs/esm';

import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 1260,
  description: 'soXXX',
  dateAdded: dayjs('2024-09-10T13:29'),
  status: 'AVAILABLE',
};

export const sampleWithPartialData: ICategory = {
  id: 5273,
  description: 'chauffeur melodic',
  dateAdded: dayjs('2024-09-09T23:18'),
  status: 'DISABLED',
};

export const sampleWithFullData: ICategory = {
  id: 21109,
  description: 'lambaste trusty',
  sortOrder: 19980,
  dateAdded: dayjs('2024-09-10T06:29'),
  dateModified: dayjs('2024-09-10T15:56'),
  status: 'DISABLED',
};

export const sampleWithNewData: NewCategory = {
  description: 'recoil piddle mmm',
  dateAdded: dayjs('2024-09-10T07:11'),
  status: 'RESTRICTED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
