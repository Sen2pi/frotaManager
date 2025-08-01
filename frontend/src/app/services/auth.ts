import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../environments/environment';

export interface AuthRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  role: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  message: string;
}

export interface User {
  id: number;
  name: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (isPlatformBrowser(this.platformId)) {
      this.loadStoredUser();
    }
  }

  /**
   * Realiza login do usuário
   */
  login(email: string, password: string): Observable<AuthResponse> {
    const payload = { email, password };
    console.log('🌐 AuthService: Making login request to:', `${this.apiUrl}/login`);
    console.log('📦 AuthService: Payload:', payload);
    
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, payload)
      .pipe(
        tap(response => {
          console.log('📥 AuthService: Response received:', response);
          if (response.token && isPlatformBrowser(this.platformId)) {
            localStorage.setItem('token', response.token);
            this.setCurrentUser(response);
          }
        })
      );
  }

  /**
   * Registra novo usuário
   */
  register(registerRequest: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, registerRequest)
      .pipe(
        tap(response => {
          if (response.token && isPlatformBrowser(this.platformId)) {
            localStorage.setItem('token', response.token);
            this.setCurrentUser(response);
          }
        })
      );
  }

  /**
   * Realiza logout
   */
  logout(): Observable<string> {
    const token = this.getToken();
    if (token) {
      return this.http.post<string>(`${this.apiUrl}/logout`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      }).pipe(
        tap(() => {
          this.clearAuth();
        })
      );
    }
    return new Observable(subscriber => {
      this.clearAuth();
      subscriber.next('Logout realizado');
      subscriber.complete();
    });
  }

  /**
   * Valida token atual
   */
  validateToken(): Observable<AuthResponse> {
    const token = this.getToken();
    if (!token) {
      return new Observable(subscriber => {
        subscriber.error('Token não encontrado');
      });
    }
    
    return this.http.post<AuthResponse>(`${this.apiUrl}/validate`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  /**
   * Obtém usuário atual
   */
  getCurrentUser(): Observable<AuthResponse> {
    const token = this.getToken();
    if (!token) {
      return new Observable(subscriber => {
        subscriber.error('Token não encontrado');
      });
    }
    
    return this.http.get<AuthResponse>(`${this.apiUrl}/me`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  /**
   * Verifica se o usuário está autenticado
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token;
  }

  /**
   * Obtém token do localStorage
   */
  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('token');
    }
    return null;
  }

  /**
   * Obtém usuário atual do BehaviorSubject
   */
  getCurrentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Define usuário atual
   */
  private setCurrentUser(authResponse: AuthResponse): void {
    const user: User = {
      id: 0, // Será preenchido pelo backend
      name: authResponse.username,
      email: authResponse.username,
      role: 'USER' // Será preenchido pelo backend
    };
    this.currentUserSubject.next(user);
  }

  /**
   * Carrega usuário armazenado
   */
  private loadStoredUser(): void {
    const token = this.getToken();
    if (token) {
      this.validateToken().subscribe({
        next: (response) => {
          this.setCurrentUser(response);
        },
        error: () => {
          this.clearAuth();
        }
      });
    }
  }

  /**
   * Limpa dados de autenticação
   */
  private clearAuth(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
    }
    this.currentUserSubject.next(null);
  }
} 