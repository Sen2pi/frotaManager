import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Chart, ChartConfiguration } from 'chart.js';
import { DashboardService, DashboardMetrics, DashboardAlert } from '../../services/dashboard';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit, AfterViewInit {
  @ViewChild('fuelChart') fuelChartRef!: ElementRef;
  @ViewChild('statusChart') statusChartRef!: ElementRef;

  // Métricas do dashboard
  totalVehicles = 0;
  totalDrivers = 0;
  maintenanceCount = 0;
  totalMileage = 0;

  // Dados para gráficos
  fuelChartData: any[] = [];
  statusChartData: any[] = [];

  // Atividades recentes
  recentActivities: any[] = [];

  // Estados de loading
  loading = false;
  error = '';

  private fuelChart?: Chart;
  private statusChart?: Chart;

  constructor(
    private dashboardService: DashboardService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngAfterViewInit(): void {
    // Aguardar um pouco para garantir que os dados foram carregados
    setTimeout(() => {
      this.initFuelChart();
      this.initStatusChart();
    }, 1000);
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = '';

    // Carregar métricas
    this.dashboardService.getMetrics().subscribe({
      next: (metrics: DashboardMetrics) => {
        this.totalVehicles = metrics.totalVehicles;
        this.totalDrivers = metrics.totalDrivers;
        this.maintenanceCount = metrics.maintenanceCount;
        this.totalMileage = metrics.totalMileage;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar métricas:', error);
        this.error = 'Erreur lors du chargement des métriques';
        this.loading = false;
        this.showSnackBar('Erreur lors du chargement des données', 'error');
      }
    });

    // Carregar atividades recentes
    this.dashboardService.getRecentActivities().subscribe({
      next: (activities) => {
        this.recentActivities = activities;
      },
      error: (error) => {
        console.error('Erro ao carregar atividades:', error);
        // Usar dados simulados se a API não estiver disponível
        this.loadMockActivities();
      }
    });
  }

  loadMockActivities(): void {
    this.recentActivities = [
      {
        icon: 'directions_car',
        title: 'Nouveau véhicule ajouté',
        description: 'Renault Clio 2023 - AB-123-CD',
        time: 'Il y a 2 heures'
      },
      {
        icon: 'build',
        title: 'Maintenance planifiée',
        description: 'Peugeot 308 - Révision générale',
        time: 'Il y a 4 heures'
      },
      {
        icon: 'person',
        title: 'Conducteur assigné',
        description: 'Jean Dupont - Véhicule BMW X3',
        time: 'Il y a 6 heures'
      },
      {
        icon: 'warning',
        title: 'Alerte maintenance',
        description: 'Audi A4 - Huile moteur faible',
        time: 'Il y a 8 heures'
      }
    ];
  }

  private initFuelChart(): void {
    if (!this.fuelChartRef) return;

    const ctx = this.fuelChartRef.nativeElement.getContext('2d');
    
    this.fuelChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'],
        datasets: [{
          label: 'Consommation (L/100km)',
          data: [8.2, 7.8, 8.5, 7.9, 8.1, 7.6],
          borderColor: '#667eea',
          backgroundColor: 'rgba(102, 126, 234, 0.1)',
          tension: 0.4,
          fill: true
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Évolution de la consommation'
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  private initStatusChart(): void {
    if (!this.statusChartRef) return;

    const ctx = this.statusChartRef.nativeElement.getContext('2d');
    
    this.statusChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['Disponible', 'En utilisation', 'Maintenance', 'Hors service'],
        datasets: [{
          data: [15, 6, 2, 1],
          backgroundColor: [
            '#4CAF50',
            '#2196F3',
            '#FF9800',
            '#F44336'
          ],
          borderWidth: 2,
          borderColor: '#fff'
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'bottom',
          },
          title: {
            display: true,
            text: 'Répartition des véhicules'
          }
        }
      }
    });
  }

  onQuickAction(action: string): void {
    switch (action) {
      case 'add-vehicle':
        this.showSnackBar('Fonctionnalité d\'ajout de véhicule en cours de développement', 'info');
        break;
      case 'add-driver':
        this.showSnackBar('Fonctionnalité d\'ajout de conducteur en cours de développement', 'info');
        break;
      case 'schedule-maintenance':
        this.showSnackBar('Fonctionnalité de planification de maintenance en cours de développement', 'info');
        break;
      case 'generate-report':
        this.showSnackBar('Fonctionnalité de génération de rapport en cours de développement', 'info');
        break;
    }
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
