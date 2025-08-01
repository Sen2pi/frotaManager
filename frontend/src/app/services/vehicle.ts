import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from '../models/vehicle';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  private apiUrl = 'http://localhost:8080/api/v1/vehicles';

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('access_token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  getVehicle(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  createVehicle(vehicle: Omit<Vehicle, 'id'>): Observable<Vehicle> {
    return this.http.post<Vehicle>(this.apiUrl, vehicle, { headers: this.getAuthHeaders() });
  }

  updateVehicle(id: number, vehicle: Partial<Vehicle>): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.apiUrl}/${id}`, vehicle, { headers: this.getAuthHeaders() });
  }

  deleteVehicle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }
}
