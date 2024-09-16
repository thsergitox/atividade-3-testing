import { IWishList, NewWishList } from './wish-list.model';

export const sampleWithRequiredData: IWishList = {
  id: 22709,
  title: 'from ruck deceivingly',
};

export const sampleWithPartialData: IWishList = {
  id: 20922,
  title: 'unless owe lest',
};

export const sampleWithFullData: IWishList = {
  id: 16245,
  title: 'composer concerning into',
  restricted: false,
};

export const sampleWithNewData: NewWishList = {
  title: 'blissful',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
