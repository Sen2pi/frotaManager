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
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { trigger, transition, style, animate } from '@angular/animations';
import { Vehicle } from '../../models/vehicle';
import { VehicleService } from '../../services/vehicle';

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
    MatPaginatorModule,
    MatSnackBarModule
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
  loading = false;
  error = '';

  constructor(
    private vehicleService: VehicleService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadVehicles();
  }

  loadVehicles(): void {
    this.loading = true;
    this.error = '';

    this.vehicleService.getVehicles().subscribe({
      next: (vehicles) => {
        this.vehicles = vehicles;
        this.totalVehicles = vehicles.length;
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar veículos:', error);
        this.error = 'Erro ao carregar veículos. Tente novamente.';
        this.loading = false;
        this.showSnackBar('Erreur lors du chargement des véhicules', 'error');
      }
    });
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
    this.showSnackBar('Fonctionnalité de modification en cours de développement', 'info');
  }

  viewDetails(vehicle: Vehicle): void {
    console.log('Voir détails:', vehicle);
    this.showSnackBar('Fonctionnalité de détails en cours de développement', 'info');
  }

  deleteVehicle(vehicle: Vehicle): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce véhicule ?')) {
      this.vehicleService.deleteVehicle(vehicle.id!).subscribe({
        next: () => {
          this.vehicles = this.vehicles.filter(v => v.id !== vehicle.id);
          this.applyFilters();
          this.showSnackBar('Véhicule supprimé avec succès', 'success');
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
          this.showSnackBar('Erreur lors de la suppression du véhicule', 'error');
        }
      });
    }
  }

  onPageChange(event: PageEvent): void {
    console.log('Page changée:', event);
    // Implementar paginação se necessário
  }

  private showSnackBar(message: string, type: 'success' | 'error' | 'info' = 'info'): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: type === 'error' ? 'error-snackbar' : type === 'success' ? 'success-snackbar' : 'info-snackbar'
    });
  }
}
