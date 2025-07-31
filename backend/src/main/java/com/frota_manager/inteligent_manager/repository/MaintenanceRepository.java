package com.frota_manager.inteligent_manager.repository;

import com.frota_manager.inteligent_manager.model.Maintenance;
import com.frota_manager.inteligent_manager.model.MaintenanceStatus;
import com.frota_manager.inteligent_manager.model.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para Maintenance com métodos de consulta personalizados
 * Utiliza Spring Data JPA para reduzir boilerplate code
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    
    /**
     * Encontra manutenções por status
     */
    List<Maintenance> findByStatus(MaintenanceStatus status);
    
    /**
     * Encontra manutenções por tipo
     */
    List<Maintenance> findByType(MaintenanceType type);
    
    /**
     * Encontra manutenções por veículo
     */
    List<Maintenance> findByVehicleId(Long vehicleId);
    
    /**
     * Encontra manutenções em progresso
     */
    @Query("SELECT m FROM Maintenance m WHERE m.status = 'IN_PROGRESS'")
    List<Maintenance> findInProgressMaintenances();
    
    /**
     * Encontra manutenções planeadas
     */
    @Query("SELECT m FROM Maintenance m WHERE m.status = 'PLANNED'")
    List<Maintenance> findPlannedMaintenances();
    
    /**
     * Encontra manutenções concluídas
     */
    @Query("SELECT m FROM Maintenance m WHERE m.status = 'COMPLETED'")
    List<Maintenance> findCompletedMaintenances();
    
    /**
     * Encontra manutenções por período
     */
    @Query("SELECT m FROM Maintenance m WHERE m.scheduledDate BETWEEN :startDate AND :endDate")
    List<Maintenance> findMaintenancesByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Encontra manutenções atrasadas
     */
    @Query("SELECT m FROM Maintenance m WHERE m.scheduledDate < :currentDate AND m.status != 'COMPLETED'")
    List<Maintenance> findOverdueMaintenances(@Param("currentDate") LocalDateTime currentDate);
    
    /**
     * Encontra manutenções por técnico
     */
    List<Maintenance> findByTechnicianNameContainingIgnoreCase(String technicianName);
    
    /**
     * Encontra manutenções por oficina
     */
    List<Maintenance> findByWorkshopNameContainingIgnoreCase(String workshopName);
    
    /**
     * Encontra manutenções com custo acima de um valor
     */
    @Query("SELECT m FROM Maintenance m WHERE m.cost > :minCost")
    List<Maintenance> findExpensiveMaintenances(@Param("minCost") BigDecimal minCost);
    
    /**
     * Conta manutenções por status
     */
    long countByStatus(MaintenanceStatus status);
    
    /**
     * Conta manutenções por tipo
     */
    long countByType(MaintenanceType type);
    
    /**
     * Conta manutenções por veículo
     */
    long countByVehicleId(Long vehicleId);
    
    /**
     * Calcula o custo total das manutenções por veículo
     */
    @Query("SELECT SUM(m.cost) FROM Maintenance m WHERE m.vehicle.id = :vehicleId AND m.status = 'COMPLETED'")
    BigDecimal getTotalCostByVehicle(@Param("vehicleId") Long vehicleId);
    
    /**
     * Encontra manutenções recentes
     */
    @Query("SELECT m FROM Maintenance m WHERE m.createdAt >= :since ORDER BY m.createdAt DESC")
    List<Maintenance> findRecentMaintenances(@Param("since") LocalDateTime since);
    
    /**
     * Encontra manutenções por descrição
     */
    List<Maintenance> findByDescriptionContainingIgnoreCase(String description);
} 