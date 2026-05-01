import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product';
import { Product } from '../../models/product';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [],
  templateUrl: './product-list.html'
})
export class ProductList implements OnInit {

  products: Product[] = [];

  constructor(
    private productService: ProductService,
    private router: Router
    ) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = [...data];
      }
    });
  }

  refresh() {
    this.loadProducts();
  }
}
