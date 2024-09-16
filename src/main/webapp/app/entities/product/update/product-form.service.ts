import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduct | NewProduct> = Omit<T, 'dateAdded' | 'dateModified'> & {
  dateAdded?: string | null;
  dateModified?: string | null;
};

type ProductFormRawValue = FormValueOf<IProduct>;

type NewProductFormRawValue = FormValueOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'dateAdded' | 'dateModified' | 'categories'>;

type ProductFormGroupContent = {
  id: FormControl<ProductFormRawValue['id'] | NewProduct['id']>;
  title: FormControl<ProductFormRawValue['title']>;
  keywords: FormControl<ProductFormRawValue['keywords']>;
  description: FormControl<ProductFormRawValue['description']>;
  rating: FormControl<ProductFormRawValue['rating']>;
  price: FormControl<ProductFormRawValue['price']>;
  quantityInStock: FormControl<ProductFormRawValue['quantityInStock']>;
  status: FormControl<ProductFormRawValue['status']>;
  weight: FormControl<ProductFormRawValue['weight']>;
  dimensions: FormControl<ProductFormRawValue['dimensions']>;
  dateAdded: FormControl<ProductFormRawValue['dateAdded']>;
  dateModified: FormControl<ProductFormRawValue['dateModified']>;
  wishList: FormControl<ProductFormRawValue['wishList']>;
  order: FormControl<ProductFormRawValue['order']>;
  categories: FormControl<ProductFormRawValue['categories']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = this.convertProductToProductRawValue({
      ...this.getFormDefaults(),
      ...product,
    });
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(productRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      }),
      keywords: new FormControl(productRawValue.keywords, {
        validators: [Validators.maxLength(200)],
      }),
      description: new FormControl(productRawValue.description, {
        validators: [Validators.minLength(10)],
      }),
      rating: new FormControl(productRawValue.rating, {
        validators: [Validators.min(0), Validators.max(5)],
      }),
      price: new FormControl(productRawValue.price, {
        validators: [Validators.required, Validators.min(0)],
      }),
      quantityInStock: new FormControl(productRawValue.quantityInStock, {
        validators: [Validators.min(0)],
      }),
      status: new FormControl(productRawValue.status, {
        validators: [Validators.required],
      }),
      weight: new FormControl(productRawValue.weight, {
        validators: [Validators.min(0)],
      }),
      dimensions: new FormControl(productRawValue.dimensions, {
        validators: [Validators.maxLength(50)],
      }),
      dateAdded: new FormControl(productRawValue.dateAdded, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(productRawValue.dateModified),
      wishList: new FormControl(productRawValue.wishList),
      order: new FormControl(productRawValue.order),
      categories: new FormControl(productRawValue.categories ?? []),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return this.convertProductRawValueToProduct(form.getRawValue() as ProductFormRawValue | NewProductFormRawValue);
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = this.convertProductToProductRawValue({ ...this.getFormDefaults(), ...product });
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateAdded: currentTime,
      dateModified: currentTime,
      categories: [],
    };
  }

  private convertProductRawValueToProduct(rawProduct: ProductFormRawValue | NewProductFormRawValue): IProduct | NewProduct {
    return {
      ...rawProduct,
      dateAdded: dayjs(rawProduct.dateAdded, DATE_TIME_FORMAT),
      dateModified: dayjs(rawProduct.dateModified, DATE_TIME_FORMAT),
    };
  }

  private convertProductToProductRawValue(
    product: IProduct | (Partial<NewProduct> & ProductFormDefaults),
  ): ProductFormRawValue | PartialWithRequiredKeyOf<NewProductFormRawValue> {
    return {
      ...product,
      dateAdded: product.dateAdded ? product.dateAdded.format(DATE_TIME_FORMAT) : undefined,
      dateModified: product.dateModified ? product.dateModified.format(DATE_TIME_FORMAT) : undefined,
      categories: product.categories ?? [],
    };
  }
}
