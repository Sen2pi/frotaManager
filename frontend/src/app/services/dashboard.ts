import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface DashboardMetrics {
  totalVehicles: number;
  totalDrivers: number;
  maintenanceCount: number;
  totalMileage: number;
  fuelConsumption: number;
  activeTrips: number;
}

export interface DashboardAlert {
  id: number;
  type: 'warning' | 'error' | 'info' | 'success';
  title: string;
  message: string;
  timestamp: Date;
}

export interface FuelStatistics {
  vehicleId: number;
  vehicleName: string;
  consumption: number;
  lastRefuel: Date;
}

export interface TopDriver {
  id: number;
  name: string;
  rating: number;
  tripsCompleted: number;
  totalMileage: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) { }

  // Obter métricas gerais
  getMetrics(): Observable<DashboardMetrics> {
    return this.http.get<DashboardMetrics>(`${this.apiUrl}/metrics`);
  }

  // Obter alertas do sistema
  getAlerts(): Observable<DashboardAlert[]> {
    return this.http.get<DashboardAlert[]>(`${this.apiUrl}/alerts`);
  }

  // Obter estatísticas de combustível
  getFuelStatistics(): Observable<FuelStatistics[]> {
    return this.http.get<FuelStatistics[]>(`${this.apiUrl}/fuel-statistics`);
  }

  // Obter melhores condutores
  getTopDrivers(): Observable<TopDriver[]> {
    return this.http.get<TopDriver[]>(`${this.apiUrl}/top-drivers`);
  }

  // Obter atividades recentes
  getRecentActivities(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/recent-activities`);
  }
}
