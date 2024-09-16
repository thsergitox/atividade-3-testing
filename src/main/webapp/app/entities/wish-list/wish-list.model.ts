import { ICustomer } from 'app/entities/customer/customer.model';

export interface IWishList {
  id: number;
  title?: string | null;
  restricted?: boolean | null;
  customer?: ICustomer | null;
}

export type NewWishList = Omit<IWishList, 'id'> & { id: null };
