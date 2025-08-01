import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Driver } from '../models/driver';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DriverService {
  private apiUrl = `${environment.apiUrl}/drivers`;

  constructor(private http: HttpClient) { }

  // Obter todos os condutores
  getDrivers(): Observable<Driver[]> {
    return this.http.get<Driver[]>(this.apiUrl);
  }

  // Obter condutor por ID
  getDriver(id: number): Observable<Driver> {
    return this.http.get<Driver>(`${this.apiUrl}/${id}`);
  }

  // Obter condutores ativos
  getActiveDrivers(): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}/active`);
  }

  // Obter condutores com licença expirando
  getDriversWithExpiringLicense(): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.apiUrl}/expiring-license`);
  }

  // Criar novo condutor
  createDriver(driver: Driver): Observable<Driver> {
    return this.http.post<Driver>(this.apiUrl, driver);
  }

  // Atualizar condutor
  updateDriver(id: number, driver: Driver): Observable<Driver> {
    return this.http.put<Driver>(`${this.apiUrl}/${id}`, driver);
  }

  // Deletar condutor
  deleteDriver(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
