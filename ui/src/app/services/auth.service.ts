import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, map, tap } from 'rxjs';
import { AuthResponse, AuthUser, LoginRequest, RegisterRequest } from '../models/auth';
import { API_BASE_URL } from '../api.constants';

type JwtPayload = Record<string, unknown> & {
  sub?: string;
  email?: string;
  name?: string;
  role?: string | string[];
  roles?: string[] | string;
  userId?: number | string;
  id?: number | string;
  exp?: number;
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'commerce_auth';
  private readonly userSubject = new BehaviorSubject<AuthUser | null>(this.loadUserFromStorage());
  readonly user$ = this.userSubject.asObservable();
  readonly isAuthenticated = signal<boolean>(!!this.getToken());

  constructor(private readonly http: HttpClient) {}

  login(request: LoginRequest) {
    return this.http.post<AuthResponse>(`${API_BASE_URL}/users/login`, request).pipe(
      tap((response) => this.saveToken(response.token)),
      map((response) => this.getCurrentUser())
    );
  }

  register(request: RegisterRequest) {
    return this.http.post<AuthResponse | void>(`${API_BASE_URL}/users/register`, request);
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    this.userSubject.next(null);
    this.isAuthenticated.set(false);
  }

  getToken(): string | null {
    return localStorage.getItem(this.storageKey);
  }

  getCurrentUser(): AuthUser | null {
    return this.userSubject.value;
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return !!user?.role && user.role.toUpperCase() === role.toUpperCase();
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  userId(): number | null {
    const user = this.getCurrentUser();
    return user?.userId ?? null;
  }

  private saveToken(token: string): void {
    localStorage.setItem(this.storageKey, token);
    const decoded = this.decodeJwt(token);
    const user: AuthUser = {
      token,
      email: decoded.email ?? decoded.sub,
      name: decoded.name ?? decoded.name ?? decoded.sub,
      role: this.extractRole(decoded),
      userId: this.extractUserId(decoded),
      username: (decoded.name as string | undefined) ?? decoded.sub,
    };
    this.userSubject.next(user);
    this.isAuthenticated.set(true);
  }

  private loadUserFromStorage(): AuthUser | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }

    const decoded = this.decodeJwt(token);
    return {
      token,
      email: decoded.email ?? decoded.sub,
      name: decoded.name ?? decoded.name ?? decoded.sub,
      role: this.extractRole(decoded),
      userId: this.extractUserId(decoded),
      username: (decoded.name as string | undefined) ?? decoded.sub,
    };
  }

  private decodeJwt(token: string): JwtPayload {
    try {
      const payload = token.split('.')[1];
      const normalized = payload.replace(/-/g, '+').replace(/_/g, '/');
      const padded = normalized.padEnd(normalized.length + (4 - (normalized.length % 4)) % 4, '=');
      return JSON.parse(atob(padded)) as JwtPayload;
    } catch {
      return {};
    }
  }

  private extractRole(decoded: JwtPayload): string | undefined {
    const role = decoded.role ?? decoded.roles ?? (decoded as { authority?: string }).authority;
    if (Array.isArray(role)) {
      return String(role[0] ?? '').replace('ROLE_', '');
    }
    if (typeof role === 'string') {
      return role.replace('ROLE_', '');
    }
    return undefined;
  }

  private extractUserId(decoded: JwtPayload): number | undefined {
    const raw = decoded.userId ?? decoded.id;
    const parsed = typeof raw === 'string' ? Number(raw) : raw;
    return Number.isFinite(parsed as number) ? (parsed as number) : undefined;
  }
}
