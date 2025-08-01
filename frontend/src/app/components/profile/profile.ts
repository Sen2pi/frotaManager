import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDividerModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.scss'
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  passwordForm!: FormGroup;
  loading = false;
  saving = false;
  user = {
    id: 1,
    name: 'Jean Dupont',
    email: 'jean.dupont@entreprise.com',
    role: 'ADMIN',
    department: 'Gestion de Flotte',
    phone: '+33 1 23 45 67 89',
    avatar: 'https://ui-avatars.com/api/?name=Jean+Dupont&background=3f51b5&color=fff',
    lastLogin: new Date('2024-01-15T10:30:00'),
    createdAt: new Date('2023-06-01T09:00:00'),
    permissions: ['VEHICLE_MANAGEMENT', 'DRIVER_MANAGEMENT', 'MAINTENANCE_MANAGEMENT', 'NOTIFICATION_MANAGEMENT']
  };

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initForms();
  }

  initForms(): void {
    this.profileForm = this.fb.group({
      name: [this.user.name, [Validators.required, Validators.minLength(2)]],
      email: [this.user.email, [Validators.required, Validators.email]],
      phone: [this.user.phone, [Validators.required]],
      department: [this.user.department, [Validators.required]]
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(group: FormGroup): {[key: string]: any} | null {
    const newPassword = group.get('newPassword')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { 'passwordMismatch': true };
  }

  onSaveProfile(): void {
    if (this.profileForm.valid) {
      this.saving = true;
      // Simular chamada à API
      setTimeout(() => {
        this.saving = false;
        this.snackBar.open('Profil mis à jour avec succès!', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }, 1500);
    }
  }

  onChangePassword(): void {
    if (this.passwordForm.valid) {
      this.saving = true;
      // Simular chamada à API
      setTimeout(() => {
        this.saving = false;
        this.passwordForm.reset();
        this.snackBar.open('Mot de passe modifié avec succès!', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }, 1500);
    }
  }

  getRoleDisplayName(role: string): string {
    const roleNames: { [key: string]: string } = {
      'ADMIN': 'Administrateur',
      'MANAGER': 'Gestionnaire',
      'OPERATOR': 'Opérateur',
      'VIEWER': 'Observateur'
    };
    return roleNames[role] || role;
  }

  getPermissionDisplayName(permission: string): string {
    const permissionNames: { [key: string]: string } = {
      'VEHICLE_MANAGEMENT': 'Gestion des Véhicules',
      'DRIVER_MANAGEMENT': 'Gestion des Conducteurs',
      'MAINTENANCE_MANAGEMENT': 'Gestion de la Maintenance',
      'NOTIFICATION_MANAGEMENT': 'Gestion des Notifications'
    };
    return permissionNames[permission] || permission;
  }
} 