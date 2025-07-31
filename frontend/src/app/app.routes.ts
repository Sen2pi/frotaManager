import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./components/dashboard/dashboard').then(m => m.DashboardComponent)
  },
  {
    path: 'vehicles',
    loadComponent: () => import('./components/vehicles/vehicles').then(m => m.VehiclesComponent)
  },
  {
    path: 'drivers',
    loadComponent: () => import('./components/drivers/drivers').then(m => m.DriversComponent)
  },
  {
    path: 'maintenance',
    loadComponent: () => import('./components/maintenance/maintenance').then(m => m.MaintenanceComponent)
  },
  {
    path: 'notifications',
    loadComponent: () => import('./components/notifications/notifications').then(m => m.NotificationsComponent)
  }
];
