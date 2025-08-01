import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatChipsModule } from '@angular/material/chips';
import { MatExpansionModule } from '@angular/material/expansion';

@Component({
  selector: 'app-settings',
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
    MatSlideToggleModule,
    MatSliderModule,
    MatChipsModule,
    MatExpansionModule
  ],
  templateUrl: './settings.html',
  styleUrl: './settings.scss'
})
export class SettingsComponent implements OnInit {
  generalForm!: FormGroup;
  notificationForm!: FormGroup;
  maintenanceForm!: FormGroup;
  securityForm!: FormGroup;
  
  loading = false;
  saving = false;
  
  // Configuration data
  settings = {
    general: {
      companyName: 'Entreprise de Transport',
      language: 'fr',
      timezone: 'Europe/Paris',
      dateFormat: 'dd/MM/yyyy',
      currency: 'EUR'
    },
    notifications: {
      emailNotifications: true,
      smsNotifications: false,
      pushNotifications: true,
      maintenanceAlerts: true,
      fuelAlerts: true,
      driverAlerts: true,
      alertFrequency: 30
    },
    maintenance: {
      autoMaintenanceReminders: true,
      maintenanceInterval: 90,
      fuelThreshold: 20,
      inspectionReminders: true,
      serviceHistory: true
    },
    security: {
      sessionTimeout: 30,
      requirePasswordChange: true,
      twoFactorAuth: false,
      loginAttempts: 3,
      passwordComplexity: 'high'
    }
  };

  languages = [
    { code: 'fr', name: 'Français' },
    { code: 'en', name: 'English' },
    { code: 'es', name: 'Español' },
    { code: 'de', name: 'Deutsch' }
  ];

  timezones = [
    { value: 'Europe/Paris', label: 'Europe/Paris (UTC+1)' },
    { value: 'Europe/London', label: 'Europe/London (UTC+0)' },
    { value: 'America/New_York', label: 'America/New_York (UTC-5)' },
    { value: 'Asia/Tokyo', label: 'Asia/Tokyo (UTC+9)' }
  ];

  currencies = [
    { code: 'EUR', name: 'Euro (€)' },
    { code: 'USD', name: 'US Dollar ($)' },
    { code: 'GBP', name: 'British Pound (£)' },
    { code: 'JPY', name: 'Japanese Yen (¥)' }
  ];

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initForms();
  }

  initForms(): void {
    this.generalForm = this.fb.group({
      companyName: [this.settings.general.companyName, [Validators.required]],
      language: [this.settings.general.language, [Validators.required]],
      timezone: [this.settings.general.timezone, [Validators.required]],
      dateFormat: [this.settings.general.dateFormat, [Validators.required]],
      currency: [this.settings.general.currency, [Validators.required]]
    });

    this.notificationForm = this.fb.group({
      emailNotifications: [this.settings.notifications.emailNotifications],
      smsNotifications: [this.settings.notifications.smsNotifications],
      pushNotifications: [this.settings.notifications.pushNotifications],
      maintenanceAlerts: [this.settings.notifications.maintenanceAlerts],
      fuelAlerts: [this.settings.notifications.fuelAlerts],
      driverAlerts: [this.settings.notifications.driverAlerts],
      alertFrequency: [this.settings.notifications.alertFrequency, [Validators.min(5), Validators.max(60)]]
    });

    this.maintenanceForm = this.fb.group({
      autoMaintenanceReminders: [this.settings.maintenance.autoMaintenanceReminders],
      maintenanceInterval: [this.settings.maintenance.maintenanceInterval, [Validators.min(30), Validators.max(365)]],
      fuelThreshold: [this.settings.maintenance.fuelThreshold, [Validators.min(5), Validators.max(50)]],
      inspectionReminders: [this.settings.maintenance.inspectionReminders],
      serviceHistory: [this.settings.maintenance.serviceHistory]
    });

    this.securityForm = this.fb.group({
      sessionTimeout: [this.settings.security.sessionTimeout, [Validators.min(5), Validators.max(120)]],
      requirePasswordChange: [this.settings.security.requirePasswordChange],
      twoFactorAuth: [this.settings.security.twoFactorAuth],
      loginAttempts: [this.settings.security.loginAttempts, [Validators.min(1), Validators.max(10)]],
      passwordComplexity: [this.settings.security.passwordComplexity, [Validators.required]]
    });
  }

  onSaveGeneral(): void {
    if (this.generalForm.valid) {
      this.saving = true;
      setTimeout(() => {
        this.saving = false;
        this.snackBar.open('Paramètres généraux sauvegardés!', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }, 1500);
    }
  }

  onSaveNotifications(): void {
    if (this.notificationForm.valid) {
      this.saving = true;
      setTimeout(() => {
        this.saving = false;
        this.snackBar.open('Paramètres de notifications sauvegardés!', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }, 1500);
    }
  }

  onSaveMaintenance(): void {
    if (this.maintenanceForm.valid) {
      this.saving = true;
      setTimeout(() => {
        this.saving = false;
        this.snackBar.open('Paramètres de maintenance sauvegardés!', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }, 1500);
    }
  }

  onSaveSecurity(): void {
    if (this.securityForm.valid) {
      this.saving = true;
      setTimeout(() => {
        this.saving = false;
        this.snackBar.open('Paramètres de sécurité sauvegardés!', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }, 1500);
    }
  }

  onResetSettings(): void {
    this.initForms();
    this.snackBar.open('Paramètres réinitialisés aux valeurs par défaut!', 'Fermer', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  onExportSettings(): void {
    const settingsData = {
      general: this.generalForm.value,
      notifications: this.notificationForm.value,
      maintenance: this.maintenanceForm.value,
      security: this.securityForm.value
    };
    
    const dataStr = JSON.stringify(settingsData, null, 2);
    const dataBlob = new Blob([dataStr], { type: 'application/json' });
    const url = URL.createObjectURL(dataBlob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'parametres_systeme.json';
    link.click();
    URL.revokeObjectURL(url);
    
    this.snackBar.open('Paramètres exportés avec succès!', 'Fermer', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  getLanguageName(code: string): string {
    const lang = this.languages.find(l => l.code === code);
    return lang ? lang.name : code;
  }

  getCurrencyName(code: string): string {
    const currency = this.currencies.find(c => c.code === code);
    return currency ? currency.name : code;
  }
} 