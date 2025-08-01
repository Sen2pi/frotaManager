import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Driver } from '../models/driver';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  private apiUrl = 'http://localhost:8080/api/v1/drivers';

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('access_token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getDrivers(): Observable<Driver[]> {
    return this.http.get<Driver[]>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  getDriver(id: number): Observable<Driver> {
    return this.http.get<Driver>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  createDriver(driver: Omit<Driver, 'id'>): Observable<Driver> {
    return this.http.post<Driver>(this.apiUrl, driver, { headers: this.getAuthHeaders() });
  }

  updateDriver(id: number, driver: Partial<Driver>): Observable<Driver> {
    return this.http.put<Driver>(`${this.apiUrl}/${id}`, driver, { headers: this.getAuthHeaders() });
  }

  deleteDriver(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }
}