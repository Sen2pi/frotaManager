import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { trigger, transition, style, animate } from '@angular/animations';

interface Notification {
  id: number;
  type: 'info' | 'warning' | 'error' | 'success';
  title: string;
  message: string;
  time: string;
  read: boolean;
}

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './notifications.html',
  styleUrl: './notifications.scss',
  animations: [
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(30px)' }),
        animate('0.6s ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class NotificationsComponent implements OnInit {
  notifications: Notification[] = [];

  constructor() {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.notifications = [
      {
        id: 1,
        type: 'warning',
        title: 'Maintenance préventive',
        message: 'Le véhicule BMW X3 nécessite une maintenance préventive dans 3 jours.',
        time: 'Il y a 2 heures',
        read: false
      },
      {
        id: 2,
        type: 'info',
        title: 'Nouveau conducteur',
        message: 'Sophie Bernard a été assignée au véhicule Mercedes C-Class.',
        time: 'Il y a 4 heures',
        read: false
      },
      {
        id: 3,
        type: 'error',
        title: 'Véhicule hors service',
        message: 'Le véhicule Volkswagen Golf est maintenant hors service pour réparation.',
        time: 'Il y a 6 heures',
        read: true
      },
      {
        id: 4,
        type: 'success',
        title: 'Maintenance terminée',
        message: 'La maintenance du véhicule Peugeot 308 a été terminée avec succès.',
        time: 'Il y a 8 heures',
        read: true
      }
    ];
  }

  getIcon(type: string): string {
    const iconMap: { [key: string]: string } = {
      'info': 'info',
      'warning': 'warning',
      'error': 'error',
      'success': 'check_circle'
    };
    return iconMap[type] || 'notifications';
  }

  markAsRead(notification: Notification): void {
    notification.read = true;
    console.log('Marquer comme lu:', notification);
  }

  deleteNotification(notification: Notification): void {
    this.notifications = this.notifications.filter(n => n.id !== notification.id);
    console.log('Supprimer notification:', notification);
  }
}
