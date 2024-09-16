import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { WishListService } from '../service/wish-list.service';
import { IWishList } from '../wish-list.model';
import { WishListFormService } from './wish-list-form.service';

import { WishListUpdateComponent } from './wish-list-update.component';

describe('WishList Management Update Component', () => {
  let comp: WishListUpdateComponent;
  let fixture: ComponentFixture<WishListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wishListFormService: WishListFormService;
  let wishListService: WishListService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WishListUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WishListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WishListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wishListFormService = TestBed.inject(WishListFormService);
    wishListService = TestBed.inject(WishListService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const wishList: IWishList = { id: 456 };
      const customer: ICustomer = { id: 12170 };
      wishList.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 14153 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const wishList: IWishList = { id: 456 };
      const customer: ICustomer = { id: 9782 };
      wishList.customer = customer;

      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.wishList).toEqual(wishList);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishList>>();
      const wishList = { id: 123 };
      jest.spyOn(wishListFormService, 'getWishList').mockReturnValue(wishList);
      jest.spyOn(wishListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wishList }));
      saveSubject.complete();

      // THEN
      expect(wishListFormService.getWishList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(wishListService.update).toHaveBeenCalledWith(expect.objectContaining(wishList));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishList>>();
      const wishList = { id: 123 };
      jest.spyOn(wishListFormService, 'getWishList').mockReturnValue({ id: null });
      jest.spyOn(wishListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wishList }));
      saveSubject.complete();

      // THEN
      expect(wishListFormService.getWishList).toHaveBeenCalled();
      expect(wishListService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishList>>();
      const wishList = { id: 123 };
      jest.spyOn(wishListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wishListService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
