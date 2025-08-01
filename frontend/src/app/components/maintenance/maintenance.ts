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
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatChipsModule,
    MatDividerModule
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

  showFilters(): void {
    console.log('🔍 Maintenance - Showing filters...');
    this.snackBar.open('Filtres de maintenance en cours de développement', 'Fermer', { duration: 3000 });
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
    ReactiveFormsModule
  ],
  template: `
    <div class="maintenance-dialog">
      <div class="dialog-header">
        <h2 mat-dialog-title>
          <mat-icon class="dialog-icon">build</mat-icon>
          {{ data.isEdit ? 'Modifier la Maintenance' : 'Planifier une Maintenance' }}
        </h2>
      </div>
      
      <form [formGroup]="maintenanceForm" (ngSubmit)="onSubmit()">
        <mat-dialog-content>
          <div class="loading-overlay" *ngIf="loading">
            <mat-spinner diameter="40"></mat-spinner>
            <p>Chargement des véhicules...</p>
          </div>
          
          <div class="form-section">
            <h3 class="section-title">
              <mat-icon>directions_car</mat-icon>
              Informations du véhicule
            </h3>
            
            <div class="form-row">
              <mat-form-field appearance="outline" class="full-width">
                <mat-label>Véhicule *</mat-label>
                <mat-select formControlName="vehicleId" required>
                  <mat-option value="">Sélectionner un véhicule</mat-option>
                  <mat-option *ngFor="let vehicle of vehicles" [value]="vehicle.id">
                    {{ getVehicleDisplayName(vehicle) }}
                  </mat-option>
                </mat-select>
                <mat-error *ngIf="getFieldError('vehicleId')">{{ getFieldError('vehicleId') }}</mat-error>
              </mat-form-field>
            </div>
          </div>
          
          <mat-divider></mat-divider>
          
          <div class="form-section">
            <h3 class="section-title">
              <mat-icon>build</mat-icon>
              Détails de la maintenance
            </h3>
            
            <div class="form-row">
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>Type de maintenance *</mat-label>
                <mat-select formControlName="type" required>
                  <mat-option value="">Sélectionner le type</mat-option>
                  <mat-option *ngFor="let type of maintenanceTypes" [value]="type">
                    {{ getTypeLabel(type) }}
                  </mat-option>
                </mat-select>
                <mat-error *ngIf="getFieldError('type')">{{ getFieldError('type') }}</mat-error>
              </mat-form-field>
              
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>Priorité *</mat-label>
                <mat-select formControlName="priority" required>
                  <mat-option value="LOW">Faible</mat-option>
                  <mat-option value="MEDIUM">Moyenne</mat-option>
                  <mat-option value="HIGH">Élevée</mat-option>
                  <mat-option value="CRITICAL">Critique</mat-option>
                </mat-select>
                <mat-error *ngIf="getFieldError('priority')">{{ getFieldError('priority') }}</mat-error>
              </mat-form-field>
            </div>
            
            <div class="form-row">
              <mat-form-field appearance="outline" class="full-width">
                <mat-label>Description *</mat-label>
                <textarea matInput formControlName="description" rows="3" 
                          placeholder="Décrivez les travaux à effectuer..." required></textarea>
                <mat-error *ngIf="getFieldError('description')">{{ getFieldError('description') }}</mat-error>
              </mat-form-field>
            </div>
          </div>
          
          <mat-divider></mat-divider>
          
          <div class="form-section">
            <h3 class="section-title">
              <mat-icon>schedule</mat-icon>
              Planification
            </h3>
            
            <div class="form-row">
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>Date planifiée *</mat-label>
                <input matInput [matDatepicker]="picker" formControlName="scheduledDate" required>
                <mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
                <mat-datepicker #picker></mat-datepicker>
                <mat-error *ngIf="getFieldError('scheduledDate')">{{ getFieldError('scheduledDate') }}</mat-error>
              </mat-form-field>
              
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>Coût estimé (€)</mat-label>
                <mat-input type="number" formControlName="estimatedCost" 
                          placeholder="0.00"></mat-input>
                <mat-error *ngIf="getFieldError('estimatedCost')">{{ getFieldError('estimatedCost') }}</mat-error>
              </mat-form-field>
            </div>
          </div>
          
          <mat-divider></mat-divider>
          
          <div class="form-section">
            <h3 class="section-title">
              <mat-icon>person</mat-icon>
              Responsables
            </h3>
            
            <div class="form-row">
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>Atelier</mat-label>
                <mat-input formControlName="workshop" placeholder="Nom de l'atelier"></mat-input>
              </mat-form-field>
              
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>Technicien</mat-label>
                <mat-input formControlName="technician" placeholder="Nom du technicien"></mat-input>
              </mat-form-field>
            </div>
            
            <div class="form-row">
              <mat-form-field appearance="outline" class="full-width">
                <mat-label>Notes additionnelles</mat-label>
                <textarea matInput formControlName="notes" rows="3" 
                          placeholder="Informations complémentaires..."></textarea>
              </mat-form-field>
            </div>
          </div>
        </mat-dialog-content>
        
        <mat-dialog-actions align="end">
          <button mat-button type="button" (click)="onCancel()">
            <mat-icon>cancel</mat-icon>
            Annuler
          </button>
          <button mat-raised-button color="primary" type="submit" 
                  [disabled]="!maintenanceForm.valid || loading">
            <mat-icon>{{ data.isEdit ? 'edit' : 'add' }}</mat-icon>
            {{ data.isEdit ? 'Modifier' : 'Créer' }}
          </button>
        </mat-dialog-actions>
      </form>
    </div>
  `,
  styles: [`
    .maintenance-dialog {
      max-width: 800px;
      width: 100%;
    }
    
    .dialog-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px;
      margin: -24px -24px 0 -24px;
      border-radius: 8px 8px 0 0;
    }
    
    .dialog-header h2 {
      margin: 0;
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 24px;
      font-weight: 600;
    }
    
    .dialog-icon {
      font-size: 28px;
      width: 28px;
      height: 28px;
    }
    
    .loading-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(255, 255, 255, 0.9);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      z-index: 1000;
      border-radius: 8px;
    }
    
    .form-section {
      margin-bottom: 24px;
    }
    
    .section-title {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #333;
      font-size: 18px;
      font-weight: 600;
      margin-bottom: 16px;
      padding-bottom: 8px;
      border-bottom: 2px solid #e0e0e0;
    }
    
    .section-title mat-icon {
      color: #667eea;
    }
    
    .form-row {
      display: flex;
      gap: 16px;
      margin-bottom: 16px;
    }
    
    .full-width {
      width: 100%;
    }
    
    .half-width {
      width: calc(50% - 8px);
    }
    
    mat-form-field {
      width: 100%;
    }
    
    .mat-mdc-form-field {
      margin-bottom: 8px;
    }
    
    mat-dialog-actions {
      padding: 16px 0;
      border-top: 1px solid #e0e0e0;
      margin-top: 16px;
    }
    
    mat-dialog-actions button {
      margin-left: 8px;
    }
    
    .mat-mdc-dialog-actions {
      justify-content: flex-end;
    }
    
    @media (max-width: 768px) {
      .form-row {
        flex-direction: column;
      }
      
      .half-width {
        width: 100%;
      }
    }
  `]
})
export class MaintenanceDialogComponent {
  maintenanceForm: FormGroup;
  vehicles: any[] = [];
  maintenanceTypes = Object.values(MaintenanceType);
  loading = false;

  constructor(
    private dialogRef: MatDialogRef<MaintenanceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private vehicleService: VehicleService,
    private snackBar: MatSnackBar
  ) {
    this.initForm();
    this.loadVehicles();
  }

  initForm(): void {
    this.maintenanceForm = this.fb.group({
      vehicleId: [this.data.maintenance?.vehicleId || '', [Validators.required]],
      type: [this.data.maintenance?.type || '', [Validators.required]],
      description: [this.data.maintenance?.description || '', [Validators.required, Validators.minLength(10)]],
      scheduledDate: [this.data.maintenance?.scheduledDate ? new Date(this.data.maintenance.scheduledDate) : '', [Validators.required]],
      estimatedCost: [this.data.maintenance?.estimatedCost || '', [Validators.min(0)]],
      workshop: [this.data.maintenance?.workshop || ''],
      technician: [this.data.maintenance?.technician || ''],
      notes: [this.data.maintenance?.notes || ''],
      priority: [this.data.maintenance?.priority || 'MEDIUM', [Validators.required]]
    });
  }

  loadVehicles(): void {
    this.loading = true;
    this.vehicleService.getVehicles().subscribe({
      next: (vehicles) => {
        this.vehicles = vehicles;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des véhicules:', error);
        this.loading = false;
        this.snackBar.open('Erreur lors du chargement des véhicules', 'Fermer', { duration: 3000 });
      }
    });
  }

  getTypeLabel(type: string): string {
    const typeMap: { [key: string]: string } = {
      'PREVENTIVE': 'Préventive',
      'CORRECTIVE': 'Corrective',
      'INSPECTION': 'Inspection',
      'REPAIR': 'Réparation',
      'REPLACEMENT': 'Remplacement'
    };
    return typeMap[type] || type;
  }

  getPriorityLabel(priority: string): string {
    const priorityMap: { [key: string]: string } = {
      'LOW': 'Faible',
      'MEDIUM': 'Moyenne',
      'HIGH': 'Élevée',
      'CRITICAL': 'Critique'
    };
    return priorityMap[priority] || priority;
  }

  getVehicleDisplayName(vehicle: any): string {
    return `${vehicle.brand} ${vehicle.model} - ${vehicle.licensePlate}`;
  }

  onSubmit(): void {
    if (this.maintenanceForm.valid) {
      const formValue = this.maintenanceForm.value;
      console.log('📝 MaintenanceDialog - Form submitted:', formValue);
      // Convertir la date en format ISO string
      if (formValue.scheduledDate) {
        formValue.scheduledDate = formValue.scheduledDate.toISOString();
      }
      this.dialogRef.close(formValue);
    } else {
      console.log('❌ MaintenanceDialog - Form invalid:', this.maintenanceForm.errors);
      this.snackBar.open('Veuillez remplir tous les champs obligatoires', 'Fermer', { duration: 3000 });
    }
  }

  onCancel(): void {
    console.log('❌ MaintenanceDialog - Cancelled');
    this.dialogRef.close();
  }

  getFieldError(fieldName: string): string {
    const field = this.maintenanceForm.get(fieldName);
    if (field && field.errors && field.touched) {
      if (field.errors['required']) {
        return 'Ce champ est obligatoire';
      }
      if (field.errors['minlength']) {
        return `Minimum ${field.errors['minlength'].requiredLength} caractères`;
      }
      if (field.errors['min']) {
        return 'La valeur doit être positive';
      }
    }
    return '';
  }
}