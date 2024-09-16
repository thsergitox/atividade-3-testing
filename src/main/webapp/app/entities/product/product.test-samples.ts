import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 30964,
  title: 'outside',
  price: 24793,
  status: 'OUT_OF_STOCK',
  dateAdded: dayjs('2024-09-10T03:37'),
};

export const sampleWithPartialData: IProduct = {
  id: 3330,
  title: 'shade given',
  keywords: 'self-reliant',
  rating: 5,
  price: 814.16,
  status: 'IN_STOCK',
  weight: 8726.18,
  dateAdded: dayjs('2024-09-09T23:24'),
};

export const sampleWithFullData: IProduct = {
  id: 28889,
  title: 'at publicise mild',
  keywords: 'kiddingly scarecrow',
  description: 'duhXXXXXXX',
  rating: 0,
  price: 10220.87,
  quantityInStock: 10215,
  status: 'OUT_OF_STOCK',
  weight: 3476.79,
  dimensions: 'barnstorm modern',
  dateAdded: dayjs('2024-09-10T11:44'),
  dateModified: dayjs('2024-09-10T11:00'),
};

export const sampleWithNewData: NewProduct = {
  title: 'boohoo if',
  price: 4374.96,
  status: 'DISCONTINUED',
  dateAdded: dayjs('2024-09-10T08:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
