import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, interval } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface Notification {
  id: number;
  type: NotificationType;
  priority: NotificationPriority;
  title: string;
  message: string;
  entityType?: string;
  entityId?: number;
  createdAt: string;
  readAt?: string;
  isRead: boolean;
  actionUrl?: string;
  expiresAt?: string;
  isExpired: boolean;
}

export interface NotificationCreate {
  type: NotificationType;
  priority: NotificationPriority;
  title: string;
  message: string;
  entityType?: string;
  entityId?: number;
  actionUrl?: string;
  expiresAt?: string;
}

export interface NotificationSummary {
  id: number;
  type: NotificationType;
  priority: NotificationPriority;
  title: string;
  createdAt: string;
  isRead: boolean;
  isExpired: boolean;
}

export interface NotificationStats {
  totalNotifications: number;
  unreadCount: number;
  criticalCount: number;
  highPriorityCount: number;
}

export enum NotificationType {
  MAINTENANCE_DUE = 'MAINTENANCE_DUE',
  MAINTENANCE_UPCOMING = 'MAINTENANCE_UPCOMING',
  MAINTENANCE_OVERDUE = 'MAINTENANCE_OVERDUE',
  LICENSE_EXPIRING = 'LICENSE_EXPIRING',
  LICENSE_EXPIRED = 'LICENSE_EXPIRED',
  LOW_FUEL = 'LOW_FUEL',
  TRIP_COMPLETED = 'TRIP_COMPLETED',
  TRIP_DELAYED = 'TRIP_DELAYED',
  VEHICLE_BREAKDOWN = 'VEHICLE_BREAKDOWN',
  VEHICLE_INSPECTION_DUE = 'VEHICLE_INSPECTION_DUE',
  COST_THRESHOLD_EXCEEDED = 'COST_THRESHOLD_EXCEEDED',
  SYSTEM_ALERT = 'SYSTEM_ALERT',
  GENERAL_INFO = 'GENERAL_INFO'
}

export enum NotificationPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = `${environment.apiUrl}/notifications`;
  
  // BehaviorSubject para notificações em tempo real
  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();
  
  private notificationsSubject = new BehaviorSubject<NotificationSummary[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  constructor(private http: HttpClient) {
    this.startPolling();
  }

  // Polling para atualizações em tempo real (a cada 30 segundos)
  private startPolling(): void {
    interval(30000) // 30 segundos
      .pipe(
        switchMap(() => this.getUnreadCount()),
        tap(count => this.unreadCountSubject.next(count))
      )
      .subscribe();

    // Carrega notificações iniciais
    this.loadUnreadNotifications();
  }

  private loadUnreadNotifications(): void {
    this.getUnreadNotifications().subscribe({
      next: (notifications) => {
        this.notificationsSubject.next(notifications);
        this.unreadCountSubject.next(notifications.length);
      },
      error: (error) => console.error('Erro ao carregar notificações:', error)
    });
  }

  // API Methods
  getAllNotifications(page: number = 0, size: number = 10, sortBy: string = 'createdAt', sortDir: string = 'desc'): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);
    
    return this.http.get<any>(this.apiUrl, { params });
  }

  getNotification(id: number): Observable<Notification> {
    return this.http.get<Notification>(`${this.apiUrl}/${id}`);
  }

  getUnreadNotifications(): Observable<NotificationSummary[]> {
    return this.http.get<NotificationSummary[]>(`${this.apiUrl}/unread`);
  }

  getUnreadNotificationsPaged(page: number = 0, size: number = 5): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<any>(`${this.apiUrl}/unread/paged`, { params });
  }

  getNotificationsByType(type: NotificationType): Observable<NotificationSummary[]> {
    return this.http.get<NotificationSummary[]>(`${this.apiUrl}/type/${type}`);
  }

  getNotificationsByPriority(priority: NotificationPriority): Observable<NotificationSummary[]> {
    return this.http.get<NotificationSummary[]>(`${this.apiUrl}/priority/${priority}`);
  }

  getNotificationsByEntity(entityType: string, entityId: number): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/entity/${entityType}/${entityId}`);
  }

  getCriticalNotifications(): Observable<NotificationSummary[]> {
    return this.http.get<NotificationSummary[]>(`${this.apiUrl}/critical`);
  }

  getHighPriorityNotifications(): Observable<NotificationSummary[]> {
    return this.http.get<NotificationSummary[]>(`${this.apiUrl}/high-priority`);
  }

  getRecentNotifications(): Observable<NotificationSummary[]> {
    return this.http.get<NotificationSummary[]>(`${this.apiUrl}/recent`);
  }

  createNotification(notification: NotificationCreate): Observable<Notification> {
    console.log('🔔 NotificationService: Creating notification', notification);
    return this.http.post<Notification>(this.apiUrl, notification);
  }

  markAsRead(id: number): Observable<Notification> {
    console.log('👁️ NotificationService: Marking notification as read', id);
    return this.http.patch<Notification>(`${this.apiUrl}/${id}/read`, {})
      .pipe(
        tap(() => this.refreshNotifications())
      );
  }

  markAllAsRead(): Observable<void> {
    console.log('👁️ NotificationService: Marking all notifications as read');
    return this.http.patch<void>(`${this.apiUrl}/read-all`, {})
      .pipe(
        tap(() => this.refreshNotifications())
      );
  }

  deleteNotification(id: number): Observable<void> {
    console.log('🗑️ NotificationService: Deleting notification', id);
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(() => this.refreshNotifications())
      );
  }

  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/unread/count`);
  }

  getNotificationStats(): Observable<NotificationStats> {
    return this.http.get<NotificationStats>(`${this.apiUrl}/stats`);
  }

  forceCheckAlerts(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/check-alerts`, {});
  }

  testConnection(): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/test`);
  }

  // Utility Methods
  refreshNotifications(): void {
    this.loadUnreadNotifications();
  }

  getPriorityColor(priority: NotificationPriority): string {
    switch (priority) {
      case NotificationPriority.CRITICAL:
        return '#f56565'; // Vermelho
      case NotificationPriority.HIGH:
        return '#ed8936'; // Laranja
      case NotificationPriority.MEDIUM:
        return '#38b2ac'; // Azul
      case NotificationPriority.LOW:
        return '#68d391'; // Verde
      default:
        return '#a0aec0'; // Cinza
    }
  }

  getPriorityIcon(priority: NotificationPriority): string {
    switch (priority) {
      case NotificationPriority.CRITICAL:
        return 'error';
      case NotificationPriority.HIGH:
        return 'warning';
      case NotificationPriority.MEDIUM:
        return 'info';
      case NotificationPriority.LOW:
        return 'check_circle';
      default:
        return 'notifications';
    }
  }

  getTypeIcon(type: NotificationType): string {
    switch (type) {
      case NotificationType.MAINTENANCE_DUE:
      case NotificationType.MAINTENANCE_UPCOMING:
      case NotificationType.MAINTENANCE_OVERDUE:
        return 'build';
      case NotificationType.LICENSE_EXPIRING:
      case NotificationType.LICENSE_EXPIRED:
        return 'card_membership';
      case NotificationType.LOW_FUEL:
        return 'local_gas_station';
      case NotificationType.TRIP_COMPLETED:
      case NotificationType.TRIP_DELAYED:
        return 'directions_car';
      case NotificationType.VEHICLE_BREAKDOWN:
      case NotificationType.VEHICLE_INSPECTION_DUE:
        return 'car_repair';
      case NotificationType.COST_THRESHOLD_EXCEEDED:
        return 'attach_money';
      case NotificationType.SYSTEM_ALERT:
        return 'computer';
      case NotificationType.GENERAL_INFO:
      default:
        return 'info';
    }
  }

  getTypeDisplayName(type: NotificationType): string {
    switch (type) {
      case NotificationType.MAINTENANCE_DUE:
        return 'Manutenção Vencida';
      case NotificationType.MAINTENANCE_UPCOMING:
        return 'Manutenção Próxima';
      case NotificationType.MAINTENANCE_OVERDUE:
        return 'Manutenção Atrasada';
      case NotificationType.LICENSE_EXPIRING:
        return 'Licença Expirando';
      case NotificationType.LICENSE_EXPIRED:
        return 'Licença Vencida';
      case NotificationType.LOW_FUEL:
        return 'Combustível Baixo';
      case NotificationType.TRIP_COMPLETED:
        return 'Viagem Concluída';
      case NotificationType.TRIP_DELAYED:
        return 'Viagem Atrasada';
      case NotificationType.VEHICLE_BREAKDOWN:
        return 'Avaria do Veículo';
      case NotificationType.VEHICLE_INSPECTION_DUE:
        return 'Inspeção Vencida';
      case NotificationType.COST_THRESHOLD_EXCEEDED:
        return 'Limite de Custo Excedido';
      case NotificationType.SYSTEM_ALERT:
        return 'Alerta do Sistema';
      case NotificationType.GENERAL_INFO:
        return 'Informação Geral';
      default:
        return type;
    }
  }

  getPriorityDisplayName(priority: NotificationPriority): string {
    switch (priority) {
      case NotificationPriority.LOW:
        return 'Baixa';
      case NotificationPriority.MEDIUM:
        return 'Média';
      case NotificationPriority.HIGH:
        return 'Alta';
      case NotificationPriority.CRITICAL:
        return 'Crítica';
      default:
        return priority;
    }
  }
}