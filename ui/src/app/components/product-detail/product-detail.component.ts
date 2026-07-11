import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Subscription, finalize } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Product, ProductPayload } from '../../models/product';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css',
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly products = inject(ProductService);
  readonly auth = inject(AuthService);

  private sub?: Subscription;
  loading = true;
  saving = false;
  error = '';
  product: Product | null = null;
  editMode = false;
  isNew = false;

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    description: ['', [Validators.required, Validators.minLength(5)]],
    price: [0, [Validators.required, Validators.min(0)]],
    stock: [0, [Validators.required, Validators.min(0)]],
    category: [''],
    imageUrl: [''],
  });

  ngOnInit(): void {
    this.sub = this.route.paramMap.subscribe((params) => {
      const rawId = params.get('id');
      this.isNew = rawId === null;
      const id = rawId && rawId !== null ? Number(rawId) : null;

      if (this.isNew) {
        this.editMode = true;
        this.loading = false;
        this.product = null;
        this.form.reset({
          name: '',
          description: '',
          price: 0,
          stock: 0,
          category: '',
          imageUrl: '',
        });
        return;
      }

      if (id) {
        this.fetch(id);
      } else {
        this.router.navigateByUrl('/products');
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  get canEdit(): boolean {
    if (this.isNew) return true;
    if (!this.product) return false;
    if (this.auth.isAdmin()) return true;

    const userId = this.auth.userId();
    return !!userId && this.product.ownerId === userId;
  }

  cancel(): void {
    this.router.navigateByUrl('/products');
  }

  save(): void {
    this.error = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: ProductPayload = this.form.getRawValue();
    this.saving = true;

    const request$ = this.isNew
      ? this.products.create(payload)
      : this.product?.id
        ? this.products.update(this.product.id, payload)
        : null;

    if (!request$) {
      this.saving = false;
      return;
    }

    request$
      .pipe(finalize(() => (this.saving = false)))
      .subscribe({
        next: (saved) => this.router.navigate(['/products', saved.id]),
        error: () => (this.error = 'Unable to save product'),
      });
  }

  delete(): void {
    if (!this.product?.id) return;
    if (!confirm('Delete this product?')) return;

    this.products.delete(this.product.id).subscribe({
      next: () => this.router.navigateByUrl('/products'),
      error: () => (this.error = 'Unable to delete product'),
    });
  }

  private fetch(id: number): void {
    this.loading = true;
    this.error = '';

    this.products
      .getById(id)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (item) => {
          this.product = item;
          this.form.patchValue({
            name: item.name,
            description: item.description,
            price: item.price,
            stock: item.stock,
            category: item.category ?? '',
            imageUrl: item.imageUrl ?? '',
          });
        },
        error: () => (this.error = 'Unable to load product'),
      });
  }
}
