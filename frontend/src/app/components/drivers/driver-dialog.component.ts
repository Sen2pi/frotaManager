import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DriverService } from '../../services/driver';
import { Driver } from '../../models/driver';

@Component({
  selector: 'app-driver-dialog',
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
    <h2 mat-dialog-title>{{ data.driver ? 'Edit Driver' : 'Add Driver' }}</h2>
    <form [formGroup]="driverForm" (ngSubmit)="onSubmit()">
      <mat-dialog-content>
        <mat-form-field appearance="outline">
          <mat-label>Name</mat-label>
          <input matInput formControlName="name" required>
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>License Number</mat-label>
          <input matInput formControlName="licenseNumber" required>
        </mat-form-field>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button (click)="onCancel()">Cancel</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="!driverForm.valid">{{ data.driver ? 'Update' : 'Create' }}</button>
      </mat-dialog-actions>
    </form>
  `,
  styles: [``]
})
export class DriverDialogComponent {
  driverForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<DriverDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { driver: Driver },
    private fb: FormBuilder,
    private driverService: DriverService,
    private snackBar: MatSnackBar
  ) {
    this.driverForm = this.fb.group({
      name: [this.data.driver?.name || '', Validators.required],
      licenseNumber: [this.data.driver?.licenseNumber || '', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.driverForm.valid) {
      const driverData = this.driverForm.value;
      if (this.data.driver) {
        this.driverService.updateDriver(this.data.driver.id!, driverData).subscribe(() => {
          this.snackBar.open('Driver updated successfully', 'Close', { duration: 3000 });
          this.dialogRef.close(true);
        });
      } else {
        this.driverService.createDriver(driverData).subscribe(() => {
          this.snackBar.open('Driver created successfully', 'Close', { duration: 3000 });
          this.dialogRef.close(true);
        });
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
