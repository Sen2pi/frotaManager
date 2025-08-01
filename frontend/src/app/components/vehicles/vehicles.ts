import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { VehicleService } from '../../services/vehicle';
import { Vehicle } from '../../models/vehicle';
import { VehicleDialogComponent } from './vehicle-dialog.component';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './vehicles.html',
  styleUrls: ['./vehicles.scss']
})
export class VehiclesComponent implements OnInit {
  displayedColumns: string[] = ['id', 'brand', 'model', 'licensePlate', 'year', 'actions'];
  dataSource!: MatTableDataSource<Vehicle>;
  vehicles: Vehicle[] = [];
  loading = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private vehicleService: VehicleService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadVehicles();
  }

  loadVehicles(): void {
    this.loading = true;
    this.vehicleService.getVehicles().subscribe({
      next: (vehicles) => {
        this.vehicles = vehicles;
        this.dataSource = new MatTableDataSource(this.vehicles);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading vehicles:', error);
        this.snackBar.open('Error loading vehicles', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  openVehicleDialog(vehicle?: Vehicle): void {
    const dialogRef = this.dialog.open(VehicleDialogComponent, {
      width: '500px',
      data: { vehicle: vehicle }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadVehicles();
      }
    });
  }

  deleteVehicle(id: number): void {
    if (confirm('Are you sure you want to delete this vehicle?')) {
      this.vehicleService.deleteVehicle(id).subscribe({
        next: () => {
          this.loadVehicles();
          this.snackBar.open('Vehicle deleted successfully', 'Close', { duration: 3000 });
        },
        error: (error) => {
          console.error('Error deleting vehicle:', error);
          this.snackBar.open('Error deleting vehicle', 'Close', { duration: 3000 });
        }
      });
    }
  }
}
