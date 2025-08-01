import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from '../models/vehicle';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private apiUrl = `${environment.apiUrl}/vehicles`;

  constructor(private http: HttpClient) { }

  // Obter todos os veículos
  getVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.apiUrl);
  }

  // Obter veículo por ID
  getVehicle(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
  }

  // Obter veículos disponíveis
  getAvailableVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/available`);
  }

  // Obter veículos que precisam de manutenção
  getVehiclesNeedingMaintenance(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/needing-maintenance`);
  }

  // Obter veículos com combustível baixo
  getVehiclesWithLowFuel(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/low-fuel`);
  }

  // Criar novo veículo
  createVehicle(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(this.apiUrl, vehicle);
  }

  // Atualizar veículo
  updateVehicle(id: number, vehicle: Vehicle): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.apiUrl}/${id}`, vehicle);
  }

  // Deletar veículo
  deleteVehicle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
