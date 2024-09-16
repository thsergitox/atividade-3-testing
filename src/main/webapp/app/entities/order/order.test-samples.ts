import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 24394,
  orderDate: dayjs('2024-09-10T11:42'),
  status: 'bland',
  totalAmount: 2744.27,
};

export const sampleWithPartialData: IOrder = {
  id: 27487,
  orderDate: dayjs('2024-09-10T06:06'),
  shippedDate: dayjs('2024-09-10T08:38'),
  status: 'suburban anti hence',
  totalAmount: 29097.69,
  trackingNumber: 'yahoo current',
};

export const sampleWithFullData: IOrder = {
  id: 9624,
  orderDate: dayjs('2024-09-10T05:01'),
  shippedDate: dayjs('2024-09-10T10:58'),
  status: 'although blow over',
  totalAmount: 31940.27,
  shippingCost: 3168.28,
  trackingNumber: 'circular',
};

export const sampleWithNewData: NewOrder = {
  orderDate: dayjs('2024-09-10T09:44'),
  status: 'boost',
  totalAmount: 9021.26,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
