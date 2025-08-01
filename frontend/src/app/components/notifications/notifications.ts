import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { trigger, transition, style, animate } from '@angular/animations';
import { NotificationService, Notification, NotificationType, NotificationPriority } from '../../services/notification';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [
    CommonModule, 
    MatCardModule, 
    MatButtonModule, 
    MatIconModule,
    MatBadgeModule,
    MatChipsModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './notifications.html',
  styleUrl: './notifications.scss',
  animations: [
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(30px)' }),
        animate('0.6s ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ]),
    trigger('slideIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateX(-100%)' }),
        animate('0.3s ease-out', style({ opacity: 1, transform: 'translateX(0)' }))
      ])
    ])
  ]
})
export class NotificationsComponent implements OnInit, OnDestroy {
  notifications: Notification[] = [];
  loading = false;
  unreadCount = 0;
  private refreshSubscription?: Subscription;
  
  // Expose enums to template
  NotificationType = NotificationType;
  NotificationPriority = NotificationPriority;

  constructor(
    private notificationService: NotificationService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadNotifications();
    this.loadUnreadCount();
    
    // Actualisation automatique toutes les 30 secondes
    this.refreshSubscription = interval(30000).subscribe(() => {
      this.loadNotifications();
      this.loadUnreadCount();
    });
  }

  ngOnDestroy(): void {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
  }

  loadNotifications(): void {
    this.loading = true;
    this.notificationService.getAllNotifications().subscribe({
      next: (response) => {
        this.notifications = response.content || [];
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des notifications:', error);
        this.snackBar.open('Erreur lors du chargement des notifications', 'Fermer', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  loadUnreadCount(): void {
    this.notificationService.getUnreadCount().subscribe({
      next: (count) => {
        this.unreadCount = count;
      },
      error: (error) => {
        console.error('Erreur lors du chargement du compteur:', error);
      }
    });
  }

  markAsRead(notification: Notification): void {
    if (!notification.isRead) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          notification.isRead = true;
          this.loadUnreadCount();
          this.snackBar.open('Notification marquée comme lue', 'Fermer', { duration: 2000 });
        },
        error: (error) => {
          console.error('Erreur:', error);
          this.snackBar.open('Erreur lors de la mise à jour', 'Fermer', { duration: 3000 });
        }
      });
    }
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.notifications.forEach(n => n.isRead = true);
        this.loadUnreadCount();
        this.snackBar.open('Toutes les notifications marquées comme lues', 'Fermer', { duration: 3000 });
      },
      error: (error) => {
        console.error('Erreur:', error);
        this.snackBar.open('Erreur lors de la mise à jour', 'Fermer', { duration: 3000 });
      }
    });
  }

  deleteNotification(notification: Notification): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette notification ?')) {
      this.notificationService.deleteNotification(notification.id).subscribe({
        next: () => {
          this.notifications = this.notifications.filter(n => n.id !== notification.id);
          this.loadUnreadCount();
          this.snackBar.open('Notification supprimée', 'Fermer', { duration: 2000 });
        },
        error: (error) => {
          console.error('Erreur:', error);
          this.snackBar.open('Erreur lors de la suppression', 'Fermer', { duration: 3000 });
        }
      });
    }
  }

  forceCheckAlerts(): void {
    this.notificationService.forceCheckAlerts().subscribe({
      next: (message) => {
        this.loadNotifications();
        this.snackBar.open('Vérification des alertes effectuée', 'Fermer', { duration: 3000 });
      },
      error: (error) => {
        console.error('Erreur:', error);
        this.snackBar.open('Erreur lors de la vérification', 'Fermer', { duration: 3000 });
      }
    });
  }

  getIcon(type: NotificationType): string {
    return this.notificationService.getTypeIcon(type);
  }

  getTypeDisplayName(type: NotificationType): string {
    return this.notificationService.getTypeDisplayName(type);
  }

  getPriorityDisplayName(priority: NotificationPriority): string {
    return this.notificationService.getPriorityDisplayName(priority);
  }

  getPriorityColor(priority: NotificationPriority): string {
    return this.notificationService.getPriorityColor(priority);
  }

  getPriorityIcon(priority: NotificationPriority): string {
    return this.notificationService.getPriorityIcon(priority);
  }

  getTimeAgo(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diffInMs = now.getTime() - date.getTime();
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60));
    const diffInHours = Math.floor(diffInMinutes / 60);
    const diffInDays = Math.floor(diffInHours / 24);

    if (diffInMinutes < 1) {
      return 'À l\'instant';
    } else if (diffInMinutes < 60) {
      return `Il y a ${diffInMinutes} minute${diffInMinutes > 1 ? 's' : ''}`;
    } else if (diffInHours < 24) {
      return `Il y a ${diffInHours} heure${diffInHours > 1 ? 's' : ''}`;
    } else if (diffInDays < 7) {
      return `Il y a ${diffInDays} jour${diffInDays > 1 ? 's' : ''}`;
    } else {
      return date.toLocaleDateString('fr-FR');
    }
  }

  onNotificationClick(notification: Notification): void {
    if (!notification.isRead) {
      this.markAsRead(notification);
    }
    
    // Navigation vers la page concernée si actionUrl est fournie
    if (notification.actionUrl) {
      // TODO: Implémenter la navigation
      console.log('Navigation vers:', notification.actionUrl);
    }
  }

  filterByPriority(priority: NotificationPriority): void {
    this.notificationService.getNotificationsByPriority(priority).subscribe({
      next: (notifications) => {
        // Convert NotificationSummary to Notification for display
        this.notifications = notifications.map(n => ({
          ...n,
          message: n.title, // Use title as message for summary
          entityType: '',
          entityId: 0,
          readAt: '',
          actionUrl: '',
          expiresAt: ''
        }));
      },
      error: (error) => {
        console.error('Erreur lors du filtrage:', error);
      }
    });
  }

  filterByType(type: NotificationType): void {
    this.notificationService.getNotificationsByType(type).subscribe({
      next: (notifications) => {
        // Convert NotificationSummary to Notification for display
        this.notifications = notifications.map(n => ({
          ...n,
          message: n.title, // Use title as message for summary
          entityType: '',
          entityId: 0,
          readAt: '',
          actionUrl: '',
          expiresAt: ''
        }));
      },
      error: (error) => {
        console.error('Erreur lors du filtrage:', error);
      }
    });
  }

  showAllNotifications(): void {
    this.loadNotifications();
  }

  getCriticalCount(): number {
    return this.notifications.filter(n => n.priority === NotificationPriority.CRITICAL).length;
  }
}