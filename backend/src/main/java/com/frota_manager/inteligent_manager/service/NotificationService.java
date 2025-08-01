package com.frota_manager.inteligent_manager.service;

import com.frota_manager.inteligent_manager.dto.NotificationCreateDto;
import com.frota_manager.inteligent_manager.dto.NotificationDto;
import com.frota_manager.inteligent_manager.dto.NotificationSummaryDto;
import com.frota_manager.inteligent_manager.model.*;
import com.frota_manager.inteligent_manager.repository.NotificationRepository;
import com.frota_manager.inteligent_manager.repository.VehicleRepository;
import com.frota_manager.inteligent_manager.repository.DriverRepository;
import com.frota_manager.inteligent_manager.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de notificações
 * Implementa alertas automáticos e notificações em tempo real
 */
@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, 
                             VehicleRepository vehicleRepository,
                             DriverRepository driverRepository,
                             MaintenanceRepository maintenanceRepository) {
        this.notificationRepository = notificationRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    /**
     * Cria uma nova notificação
     */
    public NotificationDto createNotification(NotificationCreateDto createDto) {
        System.out.println("🔔 NotificationService: Creating notification - " + createDto.title());
        
        Notification notification = new Notification();
        notification.setType(createDto.type());
        notification.setPriority(createDto.priority());
        notification.setTitle(createDto.title());
        notification.setMessage(createDto.message());
        notification.setEntityType(createDto.entityType());
        notification.setEntityId(createDto.entityId());
        notification.setActionUrl(createDto.actionUrl());
        notification.setExpiresAt(createDto.expiresAt());

        Notification saved = notificationRepository.save(notification);
        System.out.println("✅ NotificationService: Notification created with ID: " + saved.getId());
        
        return mapToDto(saved);
    }

    /**
     * Busca notificação por ID
     */
    public Optional<NotificationDto> getNotificationById(Long id) {
        return notificationRepository.findById(id)
            .map(this::mapToDto);
    }

    /**
     * Lista todas as notificações com paginação
     */
    public Page<NotificationSummaryDto> getAllNotifications(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return notificationRepository.findAll(pageable)
            .map(this::mapToSummaryDto);
    }

    /**
     * Busca notificações não lidas
     */
    public List<NotificationSummaryDto> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc()
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca notificações não lidas com paginação
     */
    public Page<NotificationSummaryDto> getUnreadNotifications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc(pageable)
            .map(this::mapToSummaryDto);
    }

    /**
     * Busca notificações por tipo
     */
    public List<NotificationSummaryDto> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByTypeOrderByCreatedAtDesc(type)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca notificações por prioridade
     */
    public List<NotificationSummaryDto> getNotificationsByPriority(NotificationPriority priority) {
        return notificationRepository.findByPriorityOrderByCreatedAtDesc(priority)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca notificações por entidade
     */
    public List<NotificationDto> getNotificationsByEntity(String entityType, Long entityId) {
        return notificationRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca notificações críticas não lidas
     */
    public List<NotificationSummaryDto> getCriticalNotifications() {
        return notificationRepository.findUnreadCriticalNotifications()
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca notificações de alta prioridade não lidas
     */
    public List<NotificationSummaryDto> getHighPriorityNotifications() {
        return notificationRepository.findUnreadHighPriorityNotifications()
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca notificações recentes (últimas 24 horas)
     */
    public List<NotificationSummaryDto> getRecentNotifications() {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        return notificationRepository.findRecentNotifications(since)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Marca notificação como lida
     */
    public NotificationDto markAsRead(Long id) {
        System.out.println("👁️ NotificationService: Marking notification as read - ID: " + id);
        
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.markAsRead();
        Notification updated = notificationRepository.save(notification);
        
        System.out.println("✅ NotificationService: Notification marked as read");
        return mapToDto(updated);
    }

    /**
     * Marca todas as notificações como lidas
     */
    public void markAllAsRead() {
        System.out.println("👁️ NotificationService: Marking all notifications as read");
        
        List<Notification> unreadNotifications = notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
        unreadNotifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(unreadNotifications);
        
        System.out.println("✅ NotificationService: All notifications marked as read");
    }

    /**
     * Remove notificação
     */
    public void deleteNotification(Long id) {
        System.out.println("🗑️ NotificationService: Deleting notification ID: " + id);
        
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }

        notificationRepository.deleteById(id);
        System.out.println("✅ NotificationService: Notification deleted successfully");
    }

    /**
     * Conta notificações não lidas
     */
    public long getUnreadCount() {
        return notificationRepository.countByIsReadFalse();
    }

    /**
     * Estatísticas de notificações
     */
    public NotificationStatsDto getNotificationStats() {
        long totalNotifications = notificationRepository.count();
        long unreadCount = notificationRepository.countByIsReadFalse();
        long criticalCount = notificationRepository.countByIsReadFalseAndPriority(NotificationPriority.CRITICAL);
        long highPriorityCount = notificationRepository.countByIsReadFalseAndPriority(NotificationPriority.HIGH);
        
        return new NotificationStatsDto(
            totalNotifications,
            unreadCount,
            criticalCount,
            highPriorityCount
        );
    }

    // === MÉTODOS DE ALERTAS AUTOMÁTICOS ===

    /**
     * Cria notificação de manutenção vencida
     */
    public void createMaintenanceDueNotification(Long vehicleId, String vehiclePlate) {
        NotificationCreateDto notification = new NotificationCreateDto(
            NotificationType.MAINTENANCE_DUE,
            NotificationPriority.HIGH,
            "Manutenção Vencida",
            "O veículo " + vehiclePlate + " precisa de manutenção urgente.",
            "Vehicle",
            vehicleId,
            "/vehicles/" + vehicleId,
            null
        );
        createNotification(notification);
    }

    /**
     * Cria notificação de licença expirando
     */
    public void createLicenseExpiringNotification(Long driverId, String driverName, LocalDate expiryDate) {
        NotificationCreateDto notification = new NotificationCreateDto(
            NotificationType.LICENSE_EXPIRING,
            NotificationPriority.HIGH,
            "Licença Expirando",
            "A licença do condutor " + driverName + " expira em " + expiryDate + ".",
            "Driver",
            driverId,
            "/drivers/" + driverId,
            null
        );
        createNotification(notification);
    }

    /**
     * Cria notificação de combustível baixo
     */
    public void createLowFuelNotification(Long vehicleId, String vehiclePlate, BigDecimal fuelLevel) {
        NotificationCreateDto notification = new NotificationCreateDto(
            NotificationType.LOW_FUEL,
            NotificationPriority.MEDIUM,
            "Combustível Baixo",
            "O veículo " + vehiclePlate + " está com combustível baixo (" + fuelLevel + "%).",
            "Vehicle",
            vehicleId,
            "/vehicles/" + vehicleId,
            null
        );
        createNotification(notification);
    }

    /**
     * Cria notificação de viagem concluída
     */
    public void createTripCompletedNotification(Long tripId, String vehiclePlate, String driverName) {
        NotificationCreateDto notification = new NotificationCreateDto(
            NotificationType.TRIP_COMPLETED,
            NotificationPriority.LOW,
            "Viagem Concluída",
            "Viagem do veículo " + vehiclePlate + " com condutor " + driverName + " foi concluída.",
            "Trip",
            tripId,
            "/trips/" + tripId,
            null
        );
        createNotification(notification);
    }

    // === VERIFICAÇÕES AUTOMÁTICAS (SCHEDULED) ===

    /**
     * Verifica e cria alertas automáticos (executa a cada hora)
     */
    @Scheduled(fixedRate = 3600000) // 1 hora
    public void checkAndCreateAutomaticAlerts() {
        System.out.println("🔍 NotificationService: Running automatic alerts check");
        
        checkMaintenanceAlerts();
        checkLicenseAlerts();
        checkFuelAlerts();
        
        System.out.println("✅ NotificationService: Automatic alerts check completed");
    }

    /**
     * Verifica alertas de manutenção
     */
    private void checkMaintenanceAlerts() {
        LocalDate today = LocalDate.now();
        LocalDate alertDate = today.plusDays(7); // Alerta 7 dias antes
        
        List<Vehicle> vehiclesDueMaintenance = vehicleRepository.findVehiclesNeedingMaintenance();
        for (Vehicle vehicle : vehiclesDueMaintenance) {
            if (vehicle.getNextMaintenanceDate() != null && 
                vehicle.getNextMaintenanceDate().isBefore(alertDate)) {
                
                // Verifica se já existe notificação similar
                List<Notification> existing = notificationRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc("Vehicle", vehicle.getId());
                boolean hasRecentAlert = existing.stream()
                    .anyMatch(n -> n.getType() == NotificationType.MAINTENANCE_DUE && 
                                   n.getCreatedAt().isAfter(LocalDateTime.now().minusDays(1)));
                
                if (!hasRecentAlert) {
                    createMaintenanceDueNotification(vehicle.getId(), vehicle.getLicensePlate());
                }
            }
        }
    }

    /**
     * Verifica alertas de licença
     */
    private void checkLicenseAlerts() {
        LocalDate today = LocalDate.now();
        LocalDate alertDate = today.plusDays(30); // Alerta 30 dias antes
        
        List<Driver> driversExpiringLicense = driverRepository.findByLicenseExpiryDateBefore(alertDate);
        for (Driver driver : driversExpiringLicense) {
            // Verifica se já existe notificação similar
            List<Notification> existing = notificationRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc("Driver", driver.getId());
            boolean hasRecentAlert = existing.stream()
                .anyMatch(n -> n.getType() == NotificationType.LICENSE_EXPIRING && 
                               n.getCreatedAt().isAfter(LocalDateTime.now().minusDays(7)));
            
            if (!hasRecentAlert) {
                createLicenseExpiringNotification(driver.getId(), driver.getName(), driver.getLicenseExpiryDate());
            }
        }
    }

    /**
     * Verifica alertas de combustível
     */
    private void checkFuelAlerts() {
        BigDecimal lowFuelThreshold = new BigDecimal("20"); // 20%
        
        List<Vehicle> lowFuelVehicles = vehicleRepository.findByCurrentFuelLevelLessThan(lowFuelThreshold);
        for (Vehicle vehicle : lowFuelVehicles) {
            // Verifica se já existe notificação similar
            List<Notification> existing = notificationRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc("Vehicle", vehicle.getId());
            boolean hasRecentAlert = existing.stream()
                .anyMatch(n -> n.getType() == NotificationType.LOW_FUEL && 
                               n.getCreatedAt().isAfter(LocalDateTime.now().minusHours(6)));
            
            if (!hasRecentAlert) {
                createLowFuelNotification(vehicle.getId(), vehicle.getLicensePlate(), vehicle.getCurrentFuelLevel());
            }
        }
    }

    /**
     * Remove notificações antigas (executa diariamente)
     */
    @Scheduled(cron = "0 0 2 * * ?") // Às 2:00 AM todos os dias
    public void cleanupOldNotifications() {
        System.out.println("🧹 NotificationService: Cleaning up old notifications");
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30); // Remove notificações com mais de 30 dias
        notificationRepository.deleteOldNotifications(cutoffDate);
        
        System.out.println("✅ NotificationService: Old notifications cleanup completed");
    }

    // Métodos de mapeamento
    private NotificationDto mapToDto(Notification notification) {
        return new NotificationDto(
            notification.getId(),
            notification.getType(),
            notification.getPriority(),
            notification.getTitle(),
            notification.getMessage(),
            notification.getEntityType(),
            notification.getEntityId(),
            notification.getCreatedAt(),
            notification.getReadAt(),
            notification.isRead(),
            notification.getActionUrl(),
            notification.getExpiresAt(),
            notification.isExpired()
        );
    }

    private NotificationSummaryDto mapToSummaryDto(Notification notification) {
        return new NotificationSummaryDto(
            notification.getId(),
            notification.getType(),
            notification.getPriority(),
            notification.getTitle(),
            notification.getCreatedAt(),
            notification.isRead(),
            notification.isExpired()
        );
    }

    // DTO para estatísticas
    public record NotificationStatsDto(
        long totalNotifications,
        long unreadCount,
        long criticalCount,
        long highPriorityCount
    ) {}
}