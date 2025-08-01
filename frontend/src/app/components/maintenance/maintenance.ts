import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { trigger, transition, style, animate } from '@angular/animations';
import { MaintenanceService, Maintenance, MaintenanceCreate, MaintenanceType, MaintenanceStatus } from '../../services/maintenance';
import { VehicleService } from '../../services/vehicle';

@Component({
  selector: 'app-maintenance',
  standalone: true,
  imports: [
    CommonModule, 
    MatCardModule, 
    MatButtonModule, 
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatIconModule
  ],
  templateUrl: './maintenance.html',
  styleUrl: './maintenance.scss',
  animations: [
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(30px)' }),
        animate('0.6s ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class MaintenanceComponent implements OnInit {
  maintenances: Maintenance[] = [];
  vehicles: any[] = [];
  loading = false;
  
  maintenanceTypes = Object.values(MaintenanceType);
  maintenanceStatuses = Object.values(MaintenanceStatus);

  constructor(
    private maintenanceService: MaintenanceService,
    private vehicleService: VehicleService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadMaintenances();
    this.loadVehicles();
  }

  loadMaintenances(): void {
    this.loading = true;
    this.maintenanceService.getMaintenances().subscribe({
      next: (response) => {
        this.maintenances = response.content || [];
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des maintenances:', error);
        this.snackBar.open('Erreur lors du chargement des maintenances', 'Fermer', { duration: 3000 });
        this.loading = false;
      }
    });
  }
  
  loadVehicles(): void {
    this.vehicleService.getVehicles().subscribe({
      next: (vehicles) => {
        this.vehicles = vehicles;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des véhicules:', error);
      }
    });
  }

  getStatusLabel(status: string): string {
    const statusMap: { [key: string]: string } = {
      'PLANNED': 'Planifiée',
      'IN_PROGRESS': 'En cours',
      'COMPLETED': 'Terminée',
      'CANCELLED': 'Annulée',
      'ON_HOLD': 'En attente'
    };
    return statusMap[status] || status;
  }

  getTypeLabel(type: string): string {
    const typeMap: { [key: string]: string } = {
      'PREVENTIVE': 'Préventive',
      'CORRECTIVE': 'Corrective',
      'INSPECTION': 'Inspection',
      'REPAIR': 'Réparation',
      'REPLACEMENT': 'Remplacement',
      'EMERGENCY': 'Urgence'
    };
    return typeMap[type] || type;
  }

  addMaintenance(): void {
    const dialogRef = this.dialog.open(MaintenanceDialogComponent, {
      width: '600px',
      data: { vehicles: this.vehicles }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.createMaintenance(result);
      }
    });
  }

  createMaintenance(maintenanceData: MaintenanceCreate): void {
    this.maintenanceService.createMaintenance(maintenanceData).subscribe({
      next: (maintenance) => {
        this.loadMaintenances();
        this.snackBar.open('Maintenance créée avec succès', 'Fermer', { duration: 3000 });
      },
      error: (error) => {
        console.error('Erreur lors de la création:', error);
        this.snackBar.open('Erreur lors de la création de la maintenance', 'Fermer', { duration: 3000 });
      }
    });
  }

  editMaintenance(maintenance: Maintenance): void {
    const dialogRef = this.dialog.open(MaintenanceDialogComponent, {
      width: '600px',
      data: { maintenance, vehicles: this.vehicles, isEdit: true }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateMaintenance(maintenance.id, result);
      }
    });
  }

  updateMaintenance(id: number, maintenanceData: any): void {
    this.maintenanceService.updateMaintenance(id, maintenanceData).subscribe({
      next: () => {
        this.loadMaintenances();
        this.snackBar.open('Maintenance mise à jour avec succès', 'Fermer', { duration: 3000 });
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour:', error);
        this.snackBar.open('Erreur lors de la mise à jour', 'Fermer', { duration: 3000 });
      }
    });
  }

  startMaintenance(maintenance: Maintenance): void {
    this.maintenanceService.startMaintenance(maintenance.id).subscribe({
      next: () => {
        this.loadMaintenances();
        this.snackBar.open('Maintenance démarrée', 'Fermer', { duration: 3000 });
      },
      error: (error) => {
        console.error('Erreur:', error);
        this.snackBar.open('Erreur lors du démarrage', 'Fermer', { duration: 3000 });
      }
    });
  }

  completeMaintenance(maintenance: Maintenance): void {
    // Ouvrir dialog pour saisir le coût réel
    const actualCost = prompt('Coût réel de la maintenance (€):');
    if (actualCost) {
      this.maintenanceService.completeMaintenance(maintenance.id, parseFloat(actualCost)).subscribe({
        next: () => {
          this.loadMaintenances();
          this.snackBar.open('Maintenance terminée', 'Fermer', { duration: 3000 });
        },
        error: (error) => {
          console.error('Erreur:', error);
          this.snackBar.open('Erreur lors de la finalisation', 'Fermer', { duration: 3000 });
        }
      });
    }
  }

  deleteMaintenance(maintenance: Maintenance): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette maintenance ?')) {
      this.maintenanceService.deleteMaintenance(maintenance.id).subscribe({
        next: () => {
          this.loadMaintenances();
          this.snackBar.open('Maintenance supprimée', 'Fermer', { duration: 3000 });
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
          this.snackBar.open('Erreur lors de la suppression', 'Fermer', { duration: 3000 });
        }
      });
    }
  }

  getVehicleInfo(vehicleId: number): string {
    const vehicle = this.vehicles.find(v => v.id === vehicleId);
    return vehicle ? `${vehicle.brand} ${vehicle.model} (${vehicle.licensePlate})` : `Véhicule #${vehicleId}`;
  }

  getTypeIcon(type: string): string {
    const iconMap: { [key: string]: string } = {
      'PREVENTIVE': 'schedule',
      'CORRECTIVE': 'build',
      'INSPECTION': 'search',
      'REPAIR': 'handyman',
      'REPLACEMENT': 'swap_horiz',
      'EMERGENCY': 'warning'
    };
    return iconMap[type] || 'build';
  }
}

// Dialog Component pour créer/modifier une maintenance
@Component({
  selector: 'app-maintenance-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatIconModule
  ],
  template: `
    <div class="dialog-container">
      <h2 mat-dialog-title class="dialog-title">
        <mat-icon>{{ data.isEdit ? 'edit' : 'add' }}</mat-icon>
        {{ data.isEdit ? 'Modifier' : 'Planifier' }} une Maintenance
      </h2>
      <form [formGroup]="maintenanceForm" (ngSubmit)="onSubmit()">
        <mat-dialog-content class="dialog-content">
          <div class="form-grid">
            <mat-form-field appearance="outline" class="form-field">
              <mat-label>Véhicule</mat-label>
              <mat-select formControlName="vehicleId" required>
                <mat-option *ngFor="let vehicle of data.vehicles" [value]="vehicle.id">
                  {{ vehicle.brand }} {{ vehicle.model }} ({{ vehicle.licensePlate }})
                </mat-option>
              </mat-select>
              <mat-icon matPrefix>directions_car</mat-icon>
              <mat-error *ngIf="maintenanceForm.get('vehicleId')?.hasError('required')">
                Le véhicule est requis.
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="form-field">
              <mat-label>Type de maintenance</mat-label>
              <mat-select formControlName="type" required>
                <mat-option value="PREVENTIVE">Préventive</mat-option>
                <mat-option value="CORRECTIVE">Corrective</mat-option>
                <mat-option value="INSPECTION">Inspection</mat-option>
                <mat-option value="REPAIR">Réparation</mat-option>
                <mat-option value="REPLACEMENT">Remplacement</mat-option>
                <mat-option value="EMERGENCY">Urgence</mat-option>
              </mat-select>
              <mat-icon matPrefix>build</mat-icon>
              <mat-error *ngIf="maintenanceForm.get('type')?.hasError('required')">
                Le type est requis.
              </mat-error>
            </mat-form-field>
          </div>

          <mat-form-field appearance="outline" class="full-width-field">
            <mat-label>Description</mat-label>
            <input matInput formControlName="description" required>
            <mat-icon matPrefix>description</mat-icon>
            <mat-error *ngIf="maintenanceForm.get('description')?.hasError('required')">
              La description est requise.
            </mat-error>
          </mat-form-field>

          <div class="form-grid">
            <mat-form-field appearance="outline" class="form-field">
              <mat-label>Date planifiée</mat-label>
              <input matInput [matDatepicker]="picker" formControlName="scheduledDate" required>
              <mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
              <mat-datepicker #picker></mat-datepicker>
              <mat-icon matPrefix>event</mat-icon>
              <mat-error *ngIf="maintenanceForm.get('scheduledDate')?.hasError('required')">
                La date est requise.
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="form-field">
              <mat-label>Coût estimé (€)</mat-label>
              <input type="number" matInput formControlName="estimatedCost" placeholder="0.00">
              <mat-icon matPrefix>euro</mat-icon>
            </mat-form-field>
          </div>

          <div class="form-grid">
            <mat-form-field appearance="outline" class="form-field">
              <mat-label>Atelier</mat-label>
              <input matInput formControlName="workshopName">
              <mat-icon matPrefix>store</mat-icon>
            </mat-form-field>

            <mat-form-field appearance="outline" class="form-field">
              <mat-label>Technicien</mat-label>
              <input matInput formControlName="technicianName">
              <mat-icon matPrefix>person</mat-icon>
            </mat-form-field>
          </div>

          <mat-form-field appearance="outline" class="full-width-field">
            <mat-label>Notes</mat-label>
            <textarea matInput formControlName="notes" rows="3"></textarea>
            <mat-icon matPrefix>notes</mat-icon>
          </mat-form-field>

        </mat-dialog-content>

        <mat-dialog-actions align="end" class="dialog-actions">
          <button mat-stroked-button type="button" (click)="onCancel()">Annuler</button>
          <button mat-raised-button color="primary" type="submit" [disabled]="!maintenanceForm.valid">
            <mat-icon>{{ data.isEdit ? 'save' : 'add' }}</mat-icon>
            {{ data.isEdit ? 'Enregistrer' : 'Créer' }}
          </button>
        </mat-dialog-actions>
      </form>
    </div>
  `,
  styles: [`
    .dialog-container {
      display: flex;
      flex-direction: column;
      height: 100%;
    }
    .dialog-title {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 1.5rem;
      font-weight: 500;
      color: #3f51b5; /* Indigo */
      border-bottom: 2px solid #3f51b5;
      padding-bottom: 16px;
      margin-bottom: 24px;
    }
    .dialog-content {
      padding-top: 10px;
      flex-grow: 1;
    }
    .form-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 20px;
      margin-bottom: 10px;
    }
    .form-field, .full-width-field {
      width: 100%;
    }
    .full-width-field {
      grid-column: 1 / -1;
      margin-bottom: 10px;
    }
    mat-form-field {
      font-size: 1rem;
    }
    mat-icon[matPrefix] {
      margin-right: 10px;
      color: #666;
    }
    .dialog-actions {
      padding: 16px 24px;
      border-top: 1px solid #eee;
      background-color: #f9f9f9;
      margin: 0 -24px -24px -24px;
    }
    button[mat-stroked-button] {
      border-radius: 20px;
      padding: 8px 24px;
    }
    button[mat-raised-button] {
      border-radius: 20px;
      padding: 8px 24px;
      mat-icon {
        margin-right: 8px;
      }
    }
    @media (max-width: 600px) {
      .form-grid {
        grid-template-columns: 1fr;
        gap: 0;
      }
    }
  `]
})
export class MaintenanceDialogComponent {
  maintenanceForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<MaintenanceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder
  ) {
    this.maintenanceForm = this.fb.group({
      vehicleId: [data.maintenance?.vehicleId || '', Validators.required],
      type: [data.maintenance?.type || '', Validators.required],
      description: [data.maintenance?.description || '', Validators.required],
      scheduledDate: [data.maintenance?.scheduledDate ? new Date(data.maintenance.scheduledDate) : '', Validators.required],
      estimatedCost: [data.maintenance?.estimatedCost || ''],
      workshopName: [data.maintenance?.workshopName || ''],
      technicianName: [data.maintenance?.technicianName || ''],
      notes: [data.maintenance?.notes || '']
    });
  }

  onSubmit(): void {
    if (this.maintenanceForm.valid) {
      const formValue = this.maintenanceForm.value;
      // Convertir la date en format ISO string
      formValue.scheduledDate = formValue.scheduledDate.toISOString();
      this.dialogRef.close(formValue);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}