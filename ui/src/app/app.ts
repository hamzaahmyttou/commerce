import { Component, signal } from '@angular/core';
import { ProductList } from './components/product-list/product-list';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ProductList],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('commerce-ui');

  goToProducts() {
    document.getElementById('products')?.scrollIntoView({ behavior: 'smooth' });
  }
}
