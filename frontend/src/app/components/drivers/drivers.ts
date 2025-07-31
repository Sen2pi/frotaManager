import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { trigger, transition, style, animate } from '@angular/animations';
import { Driver } from '../../models/driver';

@Component({
  selector: 'app-drivers',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './drivers.html',
  styleUrl: './drivers.scss',
  animations: [
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(30px)' }),
        animate('0.6s ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class DriversComponent implements OnInit {
  drivers: Driver[] = [];

  constructor() {}

  ngOnInit(): void {
    this.loadDrivers();
  }

  loadDrivers(): void {
    this.drivers = [
      {
        id: 1,
        name: 'Jean Dupont',
        licenseNumber: '123456789',
        licenseType: 'B',
        status: 'AVAILABLE',
        phoneNumber: '+33 6 12 34 56 78',
        email: 'jean.dupont@example.com',
        hireDate: new Date('2022-01-15'),
        vehicleId: 1
      },
      {
        id: 2,
        name: 'Marie Martin',
        licenseNumber: '987654321',
        licenseType: 'C',
        status: 'ON_TRIP',
        phoneNumber: '+33 6 98 76 54 32',
        email: 'marie.martin@example.com',
        hireDate: new Date('2021-06-20'),
        vehicleId: 2
      },
      {
        id: 3,
        name: 'Pierre Durand',
        licenseNumber: '456789123',
        licenseType: 'D',
        status: 'OFF_DUTY',
        phoneNumber: '+33 6 45 67 89 12',
        email: 'pierre.durand@example.com',
        hireDate: new Date('2023-03-10'),
        vehicleId: 3
      },
      {
        id: 4,
        name: 'Sophie Bernard',
        licenseNumber: '789123456',
        licenseType: 'B',
        status: 'AVAILABLE',
        phoneNumber: '+33 6 78 91 23 45',
        email: 'sophie.bernard@example.com',
        hireDate: new Date('2022-09-05'),
        vehicleId: 6
      }
    ];
  }

  getStatusLabel(status: string): string {
    const statusMap: { [key: string]: string } = {
      'AVAILABLE': 'Disponible',
      'ON_TRIP': 'En mission',
      'OFF_DUTY': 'Hors service',
      'SUSPENDED': 'Suspendu'
    };
    return statusMap[status] || status;
  }

  editDriver(driver: Driver): void {
    console.log('Modifier conducteur:', driver);
  }

  viewDetails(driver: Driver): void {
    console.log('Voir détails:', driver);
  }

  deleteDriver(driver: Driver): void {
    console.log('Supprimer conducteur:', driver);
  }
}
