import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Maintenance {
  id: number;
  vehicleId: number;
  vehiclePlate: string;
  vehicleInfo: string;
  type: MaintenanceType;
  description: string;
  scheduledDate: string;
  startDate?: string;
  completionDate?: string;
  status: MaintenanceStatus;
  estimatedCost?: number;
  actualCost?: number;
  workshop?: string;
  technician?: string;
  notes?: string;
}

export interface MaintenanceCreate {
  vehicleId: number;
  type: MaintenanceType;
  description: string;
  scheduledDate: string;
  estimatedCost?: number;
  workshop?: string;
  technician?: string;
  notes?: string;
}

export interface MaintenanceUpdate {
  type?: MaintenanceType;
  description?: string;
  scheduledDate?: string;
  status?: MaintenanceStatus;
  estimatedCost?: number;
  actualCost?: number;
  workshop?: string;
  technician?: string;
  notes?: string;
}

export interface MaintenanceSummary {
  id: number;
  vehiclePlate: string;
  type: MaintenanceType;
  description: string;
  scheduledDate: string;
  status: MaintenanceStatus;
  workshop?: string;
}

export interface MaintenanceStats {
  totalMaintenances: number;
  plannedCount: number;
  inProgressCount: number;
  completedCount: number;
  overdueCount: number;
  totalCost: number;
}

export enum MaintenanceType {
  PREVENTIVE = 'PREVENTIVE',
  CORRECTIVE = 'CORRECTIVE',
  INSPECTION = 'INSPECTION',
  REPAIR = 'REPAIR',
  REPLACEMENT = 'REPLACEMENT',
  EMERGENCY = 'EMERGENCY'
}

export enum MaintenanceStatus {
  PLANNED = 'PLANNED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  ON_HOLD = 'ON_HOLD'
}

@Injectable({
  providedIn: 'root'
})
export class MaintenanceService {
  private apiUrl = `${environment.apiUrl}/maintenances`;

  constructor(private http: HttpClient) { }

  getMaintenances(page: number = 0, size: number = 10, sortBy: string = 'scheduledDate', sortDir: string = 'desc'): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);
    
    return this.http.get<any>(this.apiUrl, { params });
  }

  getMaintenance(id: number): Observable<Maintenance> {
    return this.http.get<Maintenance>(`${this.apiUrl}/${id}`);
  }

  getMaintenancesByVehicle(vehicleId: number): Observable<Maintenance[]> {
    return this.http.get<Maintenance[]>(`${this.apiUrl}/vehicle/${vehicleId}`);
  }

  getMaintenancesByStatus(status: MaintenanceStatus): Observable<MaintenanceSummary[]> {
    return this.http.get<MaintenanceSummary[]>(`${this.apiUrl}/status/${status}`);
  }

  getOverdueMaintenances(): Observable<MaintenanceSummary[]> {
    return this.http.get<MaintenanceSummary[]>(`${this.apiUrl}/overdue`);
  }

  createMaintenance(maintenance: MaintenanceCreate): Observable<Maintenance> {
    console.log('🔧 MaintenanceService: Creating maintenance', maintenance);
    return this.http.post<Maintenance>(this.apiUrl, maintenance);
  }

  updateMaintenance(id: number, maintenance: MaintenanceUpdate): Observable<Maintenance> {
    console.log('🔧 MaintenanceService: Updating maintenance', id, maintenance);
    return this.http.put<Maintenance>(`${this.apiUrl}/${id}`, maintenance);
  }

  startMaintenance(id: number): Observable<Maintenance> {
    console.log('🚀 MaintenanceService: Starting maintenance', id);
    return this.http.patch<Maintenance>(`${this.apiUrl}/${id}/start`, {});
  }

  completeMaintenance(id: number, actualCost: number, completionNotes?: string): Observable<Maintenance> {
    console.log('🏁 MaintenanceService: Completing maintenance', id);
    const params = new HttpParams()
      .set('actualCost', actualCost.toString())
      .set('completionNotes', completionNotes || '');
    
    return this.http.patch<Maintenance>(`${this.apiUrl}/${id}/complete`, {}, { params });
  }

  deleteMaintenance(id: number): Observable<void> {
    console.log('🗑️ MaintenanceService: Deleting maintenance', id);
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getMaintenanceStats(): Observable<MaintenanceStats> {
    return this.http.get<MaintenanceStats>(`${this.apiUrl}/stats`);
  }

  testConnection(): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/test`);
  }
}