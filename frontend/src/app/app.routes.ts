import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./components/auth/login').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./components/auth/register').then(m => m.RegisterComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./components/dashboard/dashboard').then(m => m.DashboardComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'vehicles',
    loadComponent: () => import('./components/vehicles/vehicles').then(m => m.VehiclesComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'drivers',
    loadComponent: () => import('./components/drivers/drivers').then(m => m.DriversComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'maintenance',
    loadComponent: () => import('./components/maintenance/maintenance').then(m => m.MaintenanceComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'notifications',
    loadComponent: () => import('./components/notifications/notifications').then(m => m.NotificationsComponent),
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];
