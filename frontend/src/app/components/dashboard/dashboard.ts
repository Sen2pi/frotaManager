import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Chart } from 'chart.js/auto';
import { DashboardService, DashboardMetrics, DashboardAlert } from '../../services/dashboard';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';

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

  // Signals para estado reativo
  loading = signal(false);
  error = signal('');
  
  // Métricas do dashboard
  totalVehicles = signal(0);
  totalDrivers = signal(0);
  maintenanceCount = signal(0);
  totalMileage = signal(0);

  // Atividades recentes
  recentActivities = signal<any[]>([]);

  private fuelChart?: Chart;
  private statusChart?: Chart;

  constructor(
    private dashboardService: DashboardService,
    private snackBar: MatSnackBar,
    private router: Router
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
    this.loading.set(true);
    this.error.set('');

    // Carregar métricas usando signals
    this.dashboardService.getMetrics().pipe(
      catchError(error => {
        console.error('Erro ao carregar métricas:', error);
        this.error.set('Erreur lors du chargement des métriques');
        this.loading.set(false);
        this.showSnackBar('Erreur lors du chargement des données', 'error');
        return of(null);
      })
    ).subscribe(metrics => {
      if (metrics) {
        this.totalVehicles.set(metrics.totalVehicles);
        this.totalDrivers.set(metrics.totalDrivers);
        this.maintenanceCount.set(metrics.maintenanceCount);
        this.totalMileage.set(metrics.totalMileage);
      }
      this.loading.set(false);
    });

    // Carregar atividades recentes
    this.dashboardService.getRecentActivities().pipe(
      catchError(error => {
        console.error('Erro ao carregar atividades:', error);
        // Usar dados simulados se a API não estiver disponível
        this.loadMockActivities();
        return of([]);
      })
    ).subscribe(activities => {
      this.recentActivities.set(activities);
    });
  }

  loadMockActivities(): void {
    this.recentActivities.set([
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
    ]);
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
    console.log('🎯 Dashboard - Quick action clicked:', action);
    switch (action) {
      case 'add-vehicle':
        console.log('🎯 Dashboard - Navigating to vehicles page...');
        this.router.navigate(['/vehicles']);
        this.showSnackBar('Redirection vers la page des véhicules', 'info');
        break;
      case 'add-driver':
        console.log('🎯 Dashboard - Navigating to drivers page...');
        this.router.navigate(['/drivers']);
        this.showSnackBar('Redirection vers la page des conducteurs', 'info');
        break;
      case 'schedule-maintenance':
        console.log('🎯 Dashboard - Navigating to maintenance page...');
        this.router.navigate(['/maintenance']);
        this.showSnackBar('Redirection vers la page de maintenance', 'info');
        break;
      case 'generate-report':
        console.log('🎯 Dashboard - Generating report...');
        this.generateReport();
        break;
      default:
        console.log('🎯 Dashboard - Unknown action:', action);
        break;
    }
  }

  generateReport(): void {
    console.log('📊 Dashboard - Generating comprehensive report...');
    this.showSnackBar('Génération du rapport en cours...', 'info');
    
    // Simular geração de relatório
    setTimeout(() => {
      const reportData = {
        totalVehicles: this.totalVehicles(),
        totalDrivers: this.totalDrivers(),
        maintenanceCount: this.maintenanceCount(),
        totalMileage: this.totalMileage(),
        generatedAt: new Date().toLocaleString('fr-FR'),
        activities: this.recentActivities()
      };
      
      console.log('📊 Dashboard - Report data:', reportData);
      
      // Criar link de download do relatório
      const reportContent = this.formatReportContent(reportData);
      this.downloadReport(reportContent);
      
      this.showSnackBar('Rapport généré avec succès!', 'success');
    }, 2000);
  }

  private formatReportContent(data: any): string {
    return `
RAPPORT DE FLOTTE - ${data.generatedAt}

MÉTRIQUES GÉNÉRALES:
- Véhicules totaux: ${data.totalVehicles}
- Conducteurs totaux: ${data.totalDrivers}
- Maintenances en cours: ${data.maintenanceCount}
- Kilométrage total: ${data.totalMileage} km

ACTIVITÉS RÉCENTES:
${data.activities.map((activity: any) => `- ${activity.title}: ${activity.description} (${activity.time})`).join('\n')}

---
Rapport généré automatiquement par le système de gestion de flotte.
    `.trim();
  }

  private downloadReport(content: string): void {
    const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `rapport-flotte-${new Date().toISOString().split('T')[0]}.txt`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  private showSnackBar(message: string, type: 'success' | 'error' | 'info' = 'info'): void {
    console.log('🍞 Dashboard - Showing snackbar:', message, type);
    try {
      this.snackBar.open(message, 'Fermer', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
        panelClass: type === 'error' ? 'error-snackbar' : type === 'success' ? 'success-snackbar' : 'info-snackbar'
      });
      console.log('🍞 Dashboard - Snackbar opened successfully');
    } catch (error) {
      console.error('🍞 Dashboard - Error showing snackbar:', error);
    }
  }
}
