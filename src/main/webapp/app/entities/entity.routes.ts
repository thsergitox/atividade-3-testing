import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'address',
    data: { pageTitle: 'Addresses' },
    loadChildren: () => import('./address/address.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'Categories' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'Customers' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'order',
    data: { pageTitle: 'Orders' },
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'Products' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'wish-list',
    data: { pageTitle: 'WishLists' },
    loadChildren: () => import('./wish-list/wish-list.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
