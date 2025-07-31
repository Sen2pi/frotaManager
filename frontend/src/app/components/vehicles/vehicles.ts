import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { trigger, transition, style, animate } from '@angular/animations';
import { Vehicle } from '../../models/vehicle';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatPaginatorModule
  ],
  templateUrl: './vehicles.html',
  styleUrl: './vehicles.scss',
  animations: [
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(30px)' }),
        animate('0.6s ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class VehiclesComponent implements OnInit {
  vehicles: Vehicle[] = [];
  filteredVehicles: Vehicle[] = [];
  searchTerm = '';
  statusFilter = '';
  fuelFilter = '';
  pageSize = 12;
  totalVehicles = 0;

  constructor() {}

  ngOnInit(): void {
    this.loadVehicles();
    this.applyFilters();
  }

  loadVehicles(): void {
    // Données simulées
    this.vehicles = [
      {
        id: 1,
        brand: 'Renault',
        model: 'Clio',
        licensePlate: 'AB-123-CD',
        year: 2023,
        fuelType: 'GASOLINE',
        status: 'AVAILABLE',
        mileage: 15000,
        nextMaintenance: new Date('2024-06-15'),
        driverId: 1
      },
      {
        id: 2,
        brand: 'Peugeot',
        model: '308',
        licensePlate: 'EF-456-GH',
        year: 2022,
        fuelType: 'DIESEL',
        status: 'IN_USE',
        mileage: 25000,
        nextMaintenance: new Date('2024-05-20'),
        driverId: 2
      },
      {
        id: 3,
        brand: 'BMW',
        model: 'X3',
        licensePlate: 'IJ-789-KL',
        year: 2021,
        fuelType: 'HYBRID',
        status: 'MAINTENANCE',
        mileage: 35000,
        nextMaintenance: new Date('2024-04-10'),
        driverId: 3
      },
      {
        id: 4,
        brand: 'Audi',
        model: 'A4',
        licensePlate: 'MN-012-OP',
        year: 2023,
        fuelType: 'ELECTRIC',
        status: 'AVAILABLE',
        mileage: 8000,
        nextMaintenance: new Date('2024-07-01'),
        driverId: null
      },
      {
        id: 5,
        brand: 'Volkswagen',
        model: 'Golf',
        licensePlate: 'QR-345-ST',
        year: 2022,
        fuelType: 'GASOLINE',
        status: 'OUT_OF_SERVICE',
        mileage: 42000,
        nextMaintenance: new Date('2024-03-15'),
        driverId: null
      },
      {
        id: 6,
        brand: 'Mercedes',
        model: 'C-Class',
        licensePlate: 'UV-678-WX',
        year: 2023,
        fuelType: 'DIESEL',
        status: 'IN_USE',
        mileage: 18000,
        nextMaintenance: new Date('2024-06-30'),
        driverId: 4
      }
    ];
    
    this.totalVehicles = this.vehicles.length;
    this.applyFilters();
  }

  applyFilters(): void {
    this.filteredVehicles = this.vehicles.filter(vehicle => {
      const matchesSearch = !this.searchTerm || 
        vehicle.brand.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        vehicle.model.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        vehicle.licensePlate.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      const matchesStatus = !this.statusFilter || vehicle.status === this.statusFilter;
      const matchesFuel = !this.fuelFilter || vehicle.fuelType === this.fuelFilter;
      
      return matchesSearch && matchesStatus && matchesFuel;
    });
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.statusFilter = '';
    this.fuelFilter = '';
    this.applyFilters();
  }

  getStatusLabel(status: string): string {
    const statusMap: { [key: string]: string } = {
      'AVAILABLE': 'Disponible',
      'IN_USE': 'En utilisation',
      'MAINTENANCE': 'Maintenance',
      'OUT_OF_SERVICE': 'Hors service'
    };
    return statusMap[status] || status;
  }

  getFuelTypeLabel(fuelType: string): string {
    const fuelMap: { [key: string]: string } = {
      'GASOLINE': 'Essence',
      'DIESEL': 'Diesel',
      'ELECTRIC': 'Électrique',
      'HYBRID': 'Hybride'
    };
    return fuelMap[fuelType] || fuelType;
  }

  getDriverName(driverId: number): string {
    const drivers: { [key: number]: string } = {
      1: 'Jean Dupont',
      2: 'Marie Martin',
      3: 'Pierre Durand',
      4: 'Sophie Bernard'
    };
    return drivers[driverId] || 'Non assigné';
  }

  editVehicle(vehicle: Vehicle): void {
    console.log('Modifier véhicule:', vehicle);
    // Implémenter la logique de modification
  }

  viewDetails(vehicle: Vehicle): void {
    console.log('Voir détails:', vehicle);
    // Implémenter la logique d'affichage des détails
  }

  deleteVehicle(vehicle: Vehicle): void {
    console.log('Supprimer véhicule:', vehicle);
    // Implémenter la logique de suppression
  }

  onPageChange(event: PageEvent): void {
    console.log('Page changée:', event);
    // Implémenter la logique de pagination
  }
}
