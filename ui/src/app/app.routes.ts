import { Routes } from '@angular/router';
import { authGuard } from './services/auth.guard';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { AuthLoginComponent } from './components/auth-login/auth-login.component';
import { AuthRegisterComponent } from './components/auth-register/auth-register.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'products' },
  { path: 'login', component: AuthLoginComponent },
  { path: 'register', component: AuthRegisterComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'products/new', component: ProductDetailComponent, canActivate: [authGuard] },
  { path: 'products/:id', component: ProductDetailComponent },
  { path: '**', redirectTo: 'products' },
];
