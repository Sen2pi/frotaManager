package com.frota_manager.inteligent_manager.controller;

import com.frota_manager.inteligent_manager.dto.NotificationCreateDto;
import com.frota_manager.inteligent_manager.dto.NotificationDto;
import com.frota_manager.inteligent_manager.dto.NotificationSummaryDto;
import com.frota_manager.inteligent_manager.model.NotificationPriority;
import com.frota_manager.inteligent_manager.model.NotificationType;
import com.frota_manager.inteligent_manager.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de notificações
 * Fornece endpoints completos para o sistema de notificações
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Cria uma nova notificação
     * POST /api/notifications
     */
    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@Valid @RequestBody NotificationCreateDto createDto) {
        try {
            NotificationDto notification = notificationService.createNotification(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(notification);
        } catch (Exception e) {
            System.err.println("❌ Error creating notification: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca notificação por ID
     * GET /api/notifications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
            .map(notification -> ResponseEntity.ok().body(notification))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as notificações com paginação
     * GET /api/notifications?page=0&size=10&sortBy=createdAt&sortDir=desc
     */
    @GetMapping
    public ResponseEntity<Page<NotificationSummaryDto>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<NotificationSummaryDto> notifications = notificationService.getAllNotifications(page, size, sortBy, sortDir);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações não lidas
     * GET /api/notifications/unread
     */
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationSummaryDto>> getUnreadNotifications() {
        List<NotificationSummaryDto> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações não lidas com paginação
     * GET /api/notifications/unread/paged?page=0&size=5
     */
    @GetMapping("/unread/paged")
    public ResponseEntity<Page<NotificationSummaryDto>> getUnreadNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        
        Page<NotificationSummaryDto> notifications = notificationService.getUnreadNotifications(page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações por tipo
     * GET /api/notifications/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationSummaryDto>> getNotificationsByType(@PathVariable NotificationType type) {
        List<NotificationSummaryDto> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações por prioridade
     * GET /api/notifications/priority/{priority}
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<NotificationSummaryDto>> getNotificationsByPriority(@PathVariable NotificationPriority priority) {
        List<NotificationSummaryDto> notifications = notificationService.getNotificationsByPriority(priority);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações por entidade
     * GET /api/notifications/entity/{entityType}/{entityId}
     */
    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<NotificationDto>> getNotificationsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        List<NotificationDto> notifications = notificationService.getNotificationsByEntity(entityType, entityId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações críticas não lidas
     * GET /api/notifications/critical
     */
    @GetMapping("/critical")
    public ResponseEntity<List<NotificationSummaryDto>> getCriticalNotifications() {
        List<NotificationSummaryDto> notifications = notificationService.getCriticalNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações de alta prioridade não lidas
     * GET /api/notifications/high-priority
     */
    @GetMapping("/high-priority")
    public ResponseEntity<List<NotificationSummaryDto>> getHighPriorityNotifications() {
        List<NotificationSummaryDto> notifications = notificationService.getHighPriorityNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Busca notificações recentes (últimas 24 horas)
     * GET /api/notifications/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<NotificationSummaryDto>> getRecentNotifications() {
        List<NotificationSummaryDto> notifications = notificationService.getRecentNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marca notificação como lida
     * PATCH /api/notifications/{id}/read
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        try {
            NotificationDto notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            System.err.println("❌ Error marking notification as read: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Marca todas as notificações como lidas
     * PATCH /api/notifications/read-all
     */
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        try {
            notificationService.markAllAsRead();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("❌ Error marking all notifications as read: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Remove uma notificação
     * DELETE /api/notifications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("❌ Error deleting notification: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Conta notificações não lidas
     * GET /api/notifications/unread/count
     */
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount() {
        long count = notificationService.getUnreadCount();
        return ResponseEntity.ok(count);
    }

    /**
     * Estatísticas de notificações
     * GET /api/notifications/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<NotificationService.NotificationStatsDto> getNotificationStats() {
        NotificationService.NotificationStatsDto stats = notificationService.getNotificationStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Força verificação de alertas automáticos (apenas para desenvolvimento/teste)
     * POST /api/notifications/check-alerts
     */
    @PostMapping("/check-alerts")
    public ResponseEntity<String> forceCheckAlerts() {
        try {
            notificationService.checkAndCreateAutomaticAlerts();
            return ResponseEntity.ok("Verificação de alertas executada com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Error checking alerts: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro ao verificar alertas: " + e.getMessage());
        }
    }

    /**
     * Endpoint de teste
     * GET /api/notifications/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("NotificationController está funcionando!");
    }
}