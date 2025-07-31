import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Maintenance } from '../models/maintenance';

@Injectable({
  providedIn: 'root'
})
export class MaintenanceService {
  private apiUrl = 'http://localhost:8080/api/maintenance';

  constructor(private http: HttpClient) { }

  // Obter todas as manutenções
  getMaintenances(): Observable<Maintenance[]> {
    return this.http.get<Maintenance[]>(this.apiUrl);
  }

  // Obter manutenção por ID
  getMaintenance(id: number): Observable<Maintenance> {
    return this.http.get<Maintenance>(`${this.apiUrl}/${id}`);
  }

  // Obter manutenções por veículo
  getMaintenancesByVehicle(vehicleId: number): Observable<Maintenance[]> {
    return this.http.get<Maintenance[]>(`${this.apiUrl}/vehicle/${vehicleId}`);
  }

  // Obter manutenções agendadas
  getScheduledMaintenances(): Observable<Maintenance[]> {
    return this.http.get<Maintenance[]>(`${this.apiUrl}/scheduled`);
  }

  // Obter manutenções em progresso
  getInProgressMaintenances(): Observable<Maintenance[]> {
    return this.http.get<Maintenance[]>(`${this.apiUrl}/in-progress`);
  }

  // Criar nova manutenção
  createMaintenance(maintenance: Maintenance): Observable<Maintenance> {
    return this.http.post<Maintenance>(this.apiUrl, maintenance);
  }

  // Atualizar manutenção
  updateMaintenance(id: number, maintenance: Maintenance): Observable<Maintenance> {
    return this.http.put<Maintenance>(`${this.apiUrl}/${id}`, maintenance);
  }

  // Deletar manutenção
  deleteMaintenance(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
