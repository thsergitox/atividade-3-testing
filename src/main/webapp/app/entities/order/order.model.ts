import dayjs from 'dayjs/esm';
import { IAddress } from 'app/entities/address/address.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IOrder {
  id: number;
  orderDate?: dayjs.Dayjs | null;
  shippedDate?: dayjs.Dayjs | null;
  status?: string | null;
  totalAmount?: number | null;
  shippingCost?: number | null;
  trackingNumber?: string | null;
  shippingAddress?: IAddress | null;
  customer?: ICustomer | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
