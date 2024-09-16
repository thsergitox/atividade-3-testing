import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICategory, NewCategory } from '../category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategory for edit and NewCategoryFormGroupInput for create.
 */
type CategoryFormGroupInput = ICategory | PartialWithRequiredKeyOf<NewCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICategory | NewCategory> = Omit<T, 'dateAdded' | 'dateModified'> & {
  dateAdded?: string | null;
  dateModified?: string | null;
};

type CategoryFormRawValue = FormValueOf<ICategory>;

type NewCategoryFormRawValue = FormValueOf<NewCategory>;

type CategoryFormDefaults = Pick<NewCategory, 'id' | 'dateAdded' | 'dateModified' | 'products'>;

type CategoryFormGroupContent = {
  id: FormControl<CategoryFormRawValue['id'] | NewCategory['id']>;
  description: FormControl<CategoryFormRawValue['description']>;
  sortOrder: FormControl<CategoryFormRawValue['sortOrder']>;
  dateAdded: FormControl<CategoryFormRawValue['dateAdded']>;
  dateModified: FormControl<CategoryFormRawValue['dateModified']>;
  status: FormControl<CategoryFormRawValue['status']>;
  parent: FormControl<CategoryFormRawValue['parent']>;
  products: FormControl<CategoryFormRawValue['products']>;
};

export type CategoryFormGroup = FormGroup<CategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoryFormService {
  createCategoryFormGroup(category: CategoryFormGroupInput = { id: null }): CategoryFormGroup {
    const categoryRawValue = this.convertCategoryToCategoryRawValue({
      ...this.getFormDefaults(),
      ...category,
    });
    return new FormGroup<CategoryFormGroupContent>({
      id: new FormControl(
        { value: categoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(categoryRawValue.description, {
        validators: [Validators.required, Validators.minLength(5), Validators.maxLength(100)],
      }),
      sortOrder: new FormControl(categoryRawValue.sortOrder, {
        validators: [Validators.min(0)],
      }),
      dateAdded: new FormControl(categoryRawValue.dateAdded, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(categoryRawValue.dateModified),
      status: new FormControl(categoryRawValue.status, {
        validators: [Validators.required],
      }),
      parent: new FormControl(categoryRawValue.parent),
      products: new FormControl(categoryRawValue.products ?? []),
    });
  }

  getCategory(form: CategoryFormGroup): ICategory | NewCategory {
    return this.convertCategoryRawValueToCategory(form.getRawValue() as CategoryFormRawValue | NewCategoryFormRawValue);
  }

  resetForm(form: CategoryFormGroup, category: CategoryFormGroupInput): void {
    const categoryRawValue = this.convertCategoryToCategoryRawValue({ ...this.getFormDefaults(), ...category });
    form.reset(
      {
        ...categoryRawValue,
        id: { value: categoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateAdded: currentTime,
      dateModified: currentTime,
      products: [],
    };
  }

  private convertCategoryRawValueToCategory(rawCategory: CategoryFormRawValue | NewCategoryFormRawValue): ICategory | NewCategory {
    return {
      ...rawCategory,
      dateAdded: dayjs(rawCategory.dateAdded, DATE_TIME_FORMAT),
      dateModified: dayjs(rawCategory.dateModified, DATE_TIME_FORMAT),
    };
  }

  private convertCategoryToCategoryRawValue(
    category: ICategory | (Partial<NewCategory> & CategoryFormDefaults),
  ): CategoryFormRawValue | PartialWithRequiredKeyOf<NewCategoryFormRawValue> {
    return {
      ...category,
      dateAdded: category.dateAdded ? category.dateAdded.format(DATE_TIME_FORMAT) : undefined,
      dateModified: category.dateModified ? category.dateModified.format(DATE_TIME_FORMAT) : undefined,
      products: category.products ?? [],
    };
  }
}
