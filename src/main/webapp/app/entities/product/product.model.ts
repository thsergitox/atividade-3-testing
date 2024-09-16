import dayjs from 'dayjs/esm';
import { IWishList } from 'app/entities/wish-list/wish-list.model';
import { IOrder } from 'app/entities/order/order.model';
import { ICategory } from 'app/entities/category/category.model';
import { ProductStatus } from 'app/entities/enumerations/product-status.model';

export interface IProduct {
  id: number;
  title?: string | null;
  keywords?: string | null;
  description?: string | null;
  rating?: number | null;
  price?: number | null;
  quantityInStock?: number | null;
  status?: keyof typeof ProductStatus | null;
  weight?: number | null;
  dimensions?: string | null;
  dateAdded?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  wishList?: IWishList | null;
  order?: IOrder | null;
  categories?: ICategory[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
