import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, RouterModule } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { AuthService } from './services/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule,
    MatSidenavModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatMenuModule,
    MatDividerModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class AppComponent {
  title = 'Gestionnaire de Flotte';
  isHandset$: Observable<boolean>;
  currentUser$: Observable<any>;

  constructor(
    private breakpointObserver: BreakpointObserver,
    private authService: AuthService,
    private router: Router
  ) {
    console.log('🚀 AppComponent - Constructor initialized');
    this.isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset)
      .pipe(
        map(result => result.matches),
        shareReplay()
      );
    this.currentUser$ = this.authService.currentUser$;
  }

  onLogout(): void {
    console.log('🚪 AppComponent - Logout initiated');
    this.authService.logout().subscribe({
      next: () => {
        console.log('🚪 AppComponent - Logout successful, navigating to login');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('🚪 AppComponent - Logout error:', error);
        this.router.navigate(['/login']);
      }
    });
  }

  onNavigate(route: string): void {
    console.log('🧭 AppComponent - Navigating to:', route);
    this.router.navigate([route]);
  }

  isAuthenticated(): boolean {
    const isAuth = this.authService.isAuthenticated();
    console.log('🔐 AppComponent - isAuthenticated:', isAuth);
    return isAuth;
  }
}
