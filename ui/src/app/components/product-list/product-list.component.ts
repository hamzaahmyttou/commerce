import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css',
})
export class ProductListComponent implements OnInit {
  private readonly productService = inject(ProductService);
  readonly auth = inject(AuthService);

  products: Product[] = [];
  loading = true;
  error = '';

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.loading = true;
    this.error = '';

    this.productService
      .list()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (items) => (this.products = items),
        error: () => (this.error = 'Unable to load products'),
      });
  }

  remove(id?: number): void {
    if (!id) return;
    if (!confirm('Delete this product?')) return;

    this.productService.delete(id).subscribe({
      next: () => this.refresh(),
      error: () => (this.error = 'Delete failed'),
    });
  }
}
