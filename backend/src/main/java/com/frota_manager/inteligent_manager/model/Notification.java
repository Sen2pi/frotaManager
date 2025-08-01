package com.frota_manager.inteligent_manager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade para representar notificações do sistema
 * Suporta alertas automáticos para manutenção, licenças vencidas, etc.
 */
@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationPriority priority;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Column(name = "entity_type", length = 50)
    private String entityType; // Vehicle, Driver, Maintenance, Trip
    
    @Column(name = "entity_id")
    private Long entityId; // ID da entidade relacionada
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;
    
    @Column(name = "action_url", length = 500)
    private String actionUrl; // URL para ação relacionada
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // Data de expiração da notificação
    
    // Construtores
    public Notification() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Notification(NotificationType type, NotificationPriority priority, String title, String message) {
        this();
        this.type = type;
        this.priority = priority;
        this.title = title;
        this.message = message;
    }
    
    public Notification(NotificationType type, NotificationPriority priority, String title, String message, 
                       String entityType, Long entityId) {
        this(type, priority, title, message);
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public void setType(NotificationType type) {
        this.type = type;
    }
    
    public NotificationPriority getPriority() {
        return priority;
    }
    
    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public Long getEntityId() {
        return entityId;
    }
    
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getReadAt() {
        return readAt;
    }
    
    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
        this.isRead = (readAt != null);
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        this.isRead = read;
        if (read && this.readAt == null) {
            this.readAt = LocalDateTime.now();
        }
    }
    
    public String getActionUrl() {
        return actionUrl;
    }
    
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    // Métodos auxiliares
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    public void markAsRead() {
        setRead(true);
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", type=" + type +
                ", priority=" + priority +
                ", title='" + title + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}