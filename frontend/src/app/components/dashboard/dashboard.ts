import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Chart, ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit, AfterViewInit {
  @ViewChild('fuelChart') fuelChartRef!: ElementRef;
  @ViewChild('statusChart') statusChartRef!: ElementRef;

  totalVehicles = 24;
  totalDrivers = 18;
  maintenanceCount = 5;
  totalMileage = 125430;

  recentActivities = [
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

  private fuelChart?: Chart;
  private statusChart?: Chart;

  ngOnInit(): void {
    // Données simulées pour les graphiques
  }

  ngAfterViewInit(): void {
    this.initFuelChart();
    this.initStatusChart();
  }

  private initFuelChart(): void {
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
}
