import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { trigger, transition, style, animate } from '@angular/animations';
import { Maintenance } from '../../models/maintenance';

@Component({
  selector: 'app-maintenance',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
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

  constructor() {}

  ngOnInit(): void {
    this.loadMaintenances();
  }

  loadMaintenances(): void {
    this.maintenances = [
      {
        id: 1,
        vehicleId: 1,
        type: 'PREVENTIVE',
        description: 'Révision générale',
        cost: 250,
        status: 'SCHEDULED',
        scheduledDate: new Date('2024-06-15'),
        mechanicName: 'Jean Mécanique'
      },
      {
        id: 2,
        vehicleId: 3,
        type: 'CORRECTIVE',
        description: 'Réparation freins',
        cost: 180,
        status: 'IN_PROGRESS',
        scheduledDate: new Date('2024-05-20'),
        completedDate: new Date('2024-05-22'),
        mechanicName: 'Pierre Réparateur'
      },
      {
        id: 3,
        vehicleId: 2,
        type: 'INSPECTION',
        description: 'Contrôle technique',
        cost: 80,
        status: 'COMPLETED',
        scheduledDate: new Date('2024-04-10'),
        completedDate: new Date('2024-04-10'),
        mechanicName: 'Centre de contrôle'
      }
    ];
  }

  getStatusLabel(status: string): string {
    const statusMap: { [key: string]: string } = {
      'SCHEDULED': 'Planifiée',
      'IN_PROGRESS': 'En cours',
      'COMPLETED': 'Terminée',
      'CANCELLED': 'Annulée'
    };
    return statusMap[status] || status;
  }

  getTypeLabel(type: string): string {
    const typeMap: { [key: string]: string } = {
      'PREVENTIVE': 'Préventive',
      'CORRECTIVE': 'Corrective',
      'INSPECTION': 'Inspection'
    };
    return typeMap[type] || type;
  }

  editMaintenance(maintenance: Maintenance): void {
    console.log('Modifier maintenance:', maintenance);
  }

  viewDetails(maintenance: Maintenance): void {
    console.log('Voir détails:', maintenance);
  }

  deleteMaintenance(maintenance: Maintenance): void {
    console.log('Supprimer maintenance:', maintenance);
  }
}
