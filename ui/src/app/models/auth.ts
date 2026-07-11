export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface AuthUser {
  token: string;
  email?: string;
  name?: string;
  role?: string;
  userId?: number;
  username?: string;
}

export interface AuthResponse {
  token: string;
  type?: string;
  user?: {
    id?: number;
    name?: string;
    email?: string;
    role?: string;
  };
}
