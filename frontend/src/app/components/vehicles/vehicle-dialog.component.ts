import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { VehicleService } from '../../services/vehicle';
import { Vehicle } from '../../models/vehicle';

@Component({
  selector: 'app-vehicle-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ],
  template: `
    <h2 mat-dialog-title>{{ data.vehicle ? 'Edit Vehicle' : 'Add Vehicle' }}</h2>
    <form [formGroup]="vehicleForm" (ngSubmit)="onSubmit()">
      <mat-dialog-content>
        <mat-form-field appearance="outline">
          <mat-label>Brand</mat-label>
          <input matInput formControlName="brand" required>
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Model</mat-label>
          <input matInput formControlName="model" required>
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>License Plate</mat-label>
          <input matInput formControlName="licensePlate" required>
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Year</mat-label>
          <input matInput type="number" formControlName="year" required>
        </mat-form-field>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button (click)="onCancel()">Cancel</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="!vehicleForm.valid">{{ data.vehicle ? 'Update' : 'Create' }}</button>
      </mat-dialog-actions>
    </form>
  `,
  styles: [``]
})
export class VehicleDialogComponent {
  vehicleForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<VehicleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { vehicle: Vehicle },
    private fb: FormBuilder,
    private vehicleService: VehicleService,
    private snackBar: MatSnackBar
  ) {
    this.vehicleForm = this.fb.group({
      brand: [this.data.vehicle?.brand || '', Validators.required],
      model: [this.data.vehicle?.model || '', Validators.required],
      licensePlate: [this.data.vehicle?.licensePlate || '', Validators.required],
      year: [this.data.vehicle?.year || '', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.vehicleForm.valid) {
      const vehicleData = this.vehicleForm.value;
      if (this.data.vehicle) {
        this.vehicleService.updateVehicle(this.data.vehicle.id!, vehicleData).subscribe(() => {
          this.snackBar.open('Vehicle updated successfully', 'Close', { duration: 3000 });
          this.dialogRef.close(true);
        });
      } else {
        this.vehicleService.createVehicle(vehicleData).subscribe(() => {
          this.snackBar.open('Vehicle created successfully', 'Close', { duration: 3000 });
          this.dialogRef.close(true);
        });
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
