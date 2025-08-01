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
import { DriverService } from '../../services/driver';
import { Driver } from '../../models/driver';
import { DriverDialogComponent } from './driver-dialog.component';

@Component({
  selector: 'app-drivers',
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
  templateUrl: './drivers.html',
  styleUrls: ['./drivers.scss']
})
export class DriversComponent implements OnInit {
  displayedColumns: string[] = ['id', 'name', 'licenseNumber', 'actions'];
  dataSource!: MatTableDataSource<Driver>;
  drivers: Driver[] = [];
  loading = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private driverService: DriverService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadDrivers();
  }

  loadDrivers(): void {
    this.loading = true;
    this.driverService.getDrivers().subscribe({
      next: (drivers) => {
        this.drivers = drivers;
        this.dataSource = new MatTableDataSource(this.drivers);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading drivers:', error);
        this.snackBar.open('Error loading drivers', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  openDriverDialog(driver?: Driver): void {
    const dialogRef = this.dialog.open(DriverDialogComponent, {
      width: '500px',
      data: { driver: driver }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadDrivers();
      }
    });
  }

  deleteDriver(id: number): void {
    if (confirm('Are you sure you want to delete this driver?')) {
      this.driverService.deleteDriver(id).subscribe({
        next: () => {
          this.loadDrivers();
          this.snackBar.open('Driver deleted successfully', 'Close', { duration: 3000 });
        },
        error: (error) => {
          console.error('Error deleting driver:', error);
          this.snackBar.open('Error deleting driver', 'Close', { duration: 3000 });
        }
      });
    }
  }
}
