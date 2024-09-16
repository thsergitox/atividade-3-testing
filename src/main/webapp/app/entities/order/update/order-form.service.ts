import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrder | NewOrder> = Omit<T, 'orderDate' | 'shippedDate'> & {
  orderDate?: string | null;
  shippedDate?: string | null;
};

type OrderFormRawValue = FormValueOf<IOrder>;

type NewOrderFormRawValue = FormValueOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id' | 'orderDate' | 'shippedDate'>;

type OrderFormGroupContent = {
  id: FormControl<OrderFormRawValue['id'] | NewOrder['id']>;
  orderDate: FormControl<OrderFormRawValue['orderDate']>;
  shippedDate: FormControl<OrderFormRawValue['shippedDate']>;
  status: FormControl<OrderFormRawValue['status']>;
  totalAmount: FormControl<OrderFormRawValue['totalAmount']>;
  shippingCost: FormControl<OrderFormRawValue['shippingCost']>;
  trackingNumber: FormControl<OrderFormRawValue['trackingNumber']>;
  shippingAddress: FormControl<OrderFormRawValue['shippingAddress']>;
  customer: FormControl<OrderFormRawValue['customer']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderFormService {
  createOrderFormGroup(order: OrderFormGroupInput = { id: null }): OrderFormGroup {
    const orderRawValue = this.convertOrderToOrderRawValue({
      ...this.getFormDefaults(),
      ...order,
    });
    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      orderDate: new FormControl(orderRawValue.orderDate, {
        validators: [Validators.required],
      }),
      shippedDate: new FormControl(orderRawValue.shippedDate),
      status: new FormControl(orderRawValue.status, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      totalAmount: new FormControl(orderRawValue.totalAmount, {
        validators: [Validators.required, Validators.min(0)],
      }),
      shippingCost: new FormControl(orderRawValue.shippingCost, {
        validators: [Validators.min(0)],
      }),
      trackingNumber: new FormControl(orderRawValue.trackingNumber, {
        validators: [Validators.maxLength(50)],
      }),
      shippingAddress: new FormControl(orderRawValue.shippingAddress),
      customer: new FormControl(orderRawValue.customer),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return this.convertOrderRawValueToOrder(form.getRawValue() as OrderFormRawValue | NewOrderFormRawValue);
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = this.convertOrderToOrderRawValue({ ...this.getFormDefaults(), ...order });
    form.reset(
      {
        ...orderRawValue,
        id: { value: orderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      orderDate: currentTime,
      shippedDate: currentTime,
    };
  }

  private convertOrderRawValueToOrder(rawOrder: OrderFormRawValue | NewOrderFormRawValue): IOrder | NewOrder {
    return {
      ...rawOrder,
      orderDate: dayjs(rawOrder.orderDate, DATE_TIME_FORMAT),
      shippedDate: dayjs(rawOrder.shippedDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderToOrderRawValue(
    order: IOrder | (Partial<NewOrder> & OrderFormDefaults),
  ): OrderFormRawValue | PartialWithRequiredKeyOf<NewOrderFormRawValue> {
    return {
      ...order,
      orderDate: order.orderDate ? order.orderDate.format(DATE_TIME_FORMAT) : undefined,
      shippedDate: order.shippedDate ? order.shippedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
