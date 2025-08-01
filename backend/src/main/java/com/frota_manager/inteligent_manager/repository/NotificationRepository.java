package com.frota_manager.inteligent_manager.repository;

import com.frota_manager.inteligent_manager.model.Notification;
import com.frota_manager.inteligent_manager.model.NotificationPriority;
import com.frota_manager.inteligent_manager.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de banco de dados com notificações
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Busca todas as notificações não lidas
     */
    List<Notification> findByIsReadFalseOrderByCreatedAtDesc();

    /**
     * Busca notificações não lidas com paginação
     */
    Page<Notification> findByIsReadFalseOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Busca notificações por tipo
     */
    List<Notification> findByTypeOrderByCreatedAtDesc(NotificationType type);

    /**
     * Busca notificações por prioridade
     */
    List<Notification> findByPriorityOrderByCreatedAtDesc(NotificationPriority priority);

    /**
     * Busca notificações por entidade
     */
    List<Notification> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, Long entityId);

    /**
     * Busca notificações por período
     */
    List<Notification> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca notificações não expiradas
     */
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NULL OR n.expiresAt > :currentTime ORDER BY n.createdAt DESC")
    List<Notification> findActiveNotifications(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Busca notificações expiradas
     */
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt <= :currentTime ORDER BY n.createdAt DESC")
    List<Notification> findExpiredNotifications(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Busca notificações críticas não lidas
     */
    @Query("SELECT n FROM Notification n WHERE n.isRead = false AND n.priority = 'CRITICAL' ORDER BY n.createdAt DESC")
    List<Notification> findUnreadCriticalNotifications();

    /**
     * Busca notificações de alta prioridade não lidas
     */
    @Query("SELECT n FROM Notification n WHERE n.isRead = false AND n.priority IN ('HIGH', 'CRITICAL') ORDER BY n.createdAt DESC")
    List<Notification> findUnreadHighPriorityNotifications();

    /**
     * Conta notificações não lidas
     */
    long countByIsReadFalse();

    /**
     * Conta notificações não lidas por prioridade
     */
    long countByIsReadFalseAndPriority(NotificationPriority priority);

    /**
     * Conta notificações por tipo
     */
    long countByType(NotificationType type);

    /**
     * Busca notificações recentes (últimas 24 horas)
     */
    @Query("SELECT n FROM Notification n WHERE n.createdAt >= :since ORDER BY n.createdAt DESC")
    List<Notification> findRecentNotifications(@Param("since") LocalDateTime since);

    /**
     * Busca notificações por múltiplos tipos
     */
    @Query("SELECT n FROM Notification n WHERE n.type IN :types ORDER BY n.createdAt DESC")
    List<Notification> findByTypesIn(@Param("types") List<NotificationType> types);

    /**
     * Remove notificações antigas (mais de X dias)
     */
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
    void deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Busca notificações por título (busca parcial)
     */
    List<Notification> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);

    /**
     * Busca notificações por mensagem (busca parcial)
     */
    List<Notification> findByMessageContainingIgnoreCaseOrderByCreatedAtDesc(String message);

    /**
     * Marca todas as notificações de uma entidade como lidas
     */
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readTime WHERE n.entityType = :entityType AND n.entityId = :entityId AND n.isRead = false")
    void markAllAsReadByEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId, @Param("readTime") LocalDateTime readTime);

    /**
     * Marca todas as notificações de um tipo como lidas
     */
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readTime WHERE n.type = :type AND n.isRead = false")
    void markAllAsReadByType(@Param("type") NotificationType type, @Param("readTime") LocalDateTime readTime);
}