export interface Product {
  id?: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  category?: string;
  imageUrl?: string;
  ownerId?: number;
  ownerUsername?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ProductPayload {
  name: string;
  description: string;
  price: number;
  stock: number;
  category?: string;
  imageUrl?: string;
}
