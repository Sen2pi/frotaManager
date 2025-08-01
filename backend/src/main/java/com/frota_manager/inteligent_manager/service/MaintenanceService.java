package com.frota_manager.inteligent_manager.service;

import com.frota_manager.inteligent_manager.dto.CreateMaintenanceDto;
import com.frota_manager.inteligent_manager.dto.MaintenanceDto;
import com.frota_manager.inteligent_manager.dto.MaintenanceSummaryDto;
import com.frota_manager.inteligent_manager.dto.UpdateMaintenanceDto;
import com.frota_manager.inteligent_manager.model.Maintenance;
import com.frota_manager.inteligent_manager.model.MaintenanceStatus;
import com.frota_manager.inteligent_manager.model.MaintenanceType;
import com.frota_manager.inteligent_manager.model.Vehicle;
import com.frota_manager.inteligent_manager.repository.MaintenanceRepository;
import com.frota_manager.inteligent_manager.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de manutenções
 * Implementa toda a lógica de negócio para o módulo de manutenção
 */
@Service
@Transactional
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository, VehicleRepository vehicleRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Cria uma nova manutenção
     */
    public MaintenanceDto createMaintenance(CreateMaintenanceDto createDto) {
        System.out.println("🔧 MaintenanceService: Creating maintenance for vehicle ID: " + createDto.vehicleId());
        
        Vehicle vehicle = vehicleRepository.findById(createDto.vehicleId())
            .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + createDto.vehicleId()));

        Maintenance maintenance = new Maintenance();
        maintenance.setVehicle(vehicle);
        maintenance.setType(createDto.type());
        maintenance.setDescription(createDto.description());
        maintenance.setScheduledDate(createDto.scheduledDate());
        maintenance.setStatus(MaintenanceStatus.PLANNED);
        maintenance.setEstimatedCost(createDto.estimatedCost());
        maintenance.setWorkshop(createDto.workshop());
        maintenance.setTechnician(createDto.technician());
        maintenance.setNotes(createDto.notes());

        Maintenance saved = maintenanceRepository.save(maintenance);
        System.out.println("✅ MaintenanceService: Maintenance created with ID: " + saved.getId());
        
        return mapToDto(saved);
    }

    /**
     * Busca manutenção por ID
     */
    public Optional<MaintenanceDto> getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id)
            .map(this::mapToDto);
    }

    /**
     * Lista todas as manutenções com paginação
     */
    public Page<MaintenanceSummaryDto> getAllMaintenances(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return maintenanceRepository.findAll(pageable)
            .map(this::mapToSummaryDto);
    }

    /**
     * Busca manutenções por veículo
     */
    public List<MaintenanceDto> getMaintenancesByVehicle(Long vehicleId) {
        return maintenanceRepository.findByVehicleIdOrderByScheduledDateDesc(vehicleId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca manutenções por status
     */
    public List<MaintenanceSummaryDto> getMaintenancesByStatus(MaintenanceStatus status) {
        return maintenanceRepository.findByStatusOrderByScheduledDateAsc(status)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca manutenções por tipo
     */
    public List<MaintenanceSummaryDto> getMaintenancesByType(MaintenanceType type) {
        return maintenanceRepository.findByTypeOrderByScheduledDateDesc(type)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca manutenções programadas para período
     */
    public List<MaintenanceSummaryDto> getScheduledMaintenances(LocalDate startDate, LocalDate endDate) {
        return maintenanceRepository.findByScheduledDateBetweenOrderByScheduledDateAsc(startDate, endDate)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca manutenções vencidas (planned/in_progress que passaram da data)
     */
    public List<MaintenanceSummaryDto> getOverdueMaintenances() {
        return maintenanceRepository.findOverdueMaintenances(LocalDateTime.now())
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca manutenções por custo (entre valores)
     */
    public List<MaintenanceSummaryDto> getMaintenancesByCostRange(BigDecimal minCost, BigDecimal maxCost) {
        return maintenanceRepository.findByActualCostBetweenOrderByCostDesc(minCost, maxCost)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca manutenções por oficina
     */
    public List<MaintenanceSummaryDto> getMaintenancesByWorkshop(String workshop) {
        return maintenanceRepository.findByWorkshopContainingIgnoreCaseOrderByScheduledDateDesc(workshop)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Atualiza uma manutenção
     */
    public MaintenanceDto updateMaintenance(Long id, UpdateMaintenanceDto updateDto) {
        System.out.println("🔧 MaintenanceService: Updating maintenance ID: " + id);
        
        Maintenance maintenance = maintenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        // Atualiza campos se fornecidos
        if (updateDto.type() != null) {
            maintenance.setType(updateDto.type());
        }
        if (updateDto.description() != null) {
            maintenance.setDescription(updateDto.description());
        }
        if (updateDto.scheduledDate() != null) {
            maintenance.setScheduledDate(updateDto.scheduledDate());
        }
        if (updateDto.status() != null) {
            maintenance.setStatus(updateDto.status());
            
            // Atualiza timestamps baseado no status
            if (updateDto.status() == MaintenanceStatus.IN_PROGRESS && maintenance.getStartDate() == null) {
                maintenance.setStartDate(LocalDateTime.now());
            }
            if (updateDto.status() == MaintenanceStatus.COMPLETED && maintenance.getCompletionDate() == null) {
                maintenance.setCompletionDate(LocalDateTime.now());
            }
        }
        if (updateDto.estimatedCost() != null) {
            maintenance.setEstimatedCost(updateDto.estimatedCost());
        }
        if (updateDto.actualCost() != null) {
            maintenance.setActualCost(updateDto.actualCost());
        }
        if (updateDto.workshop() != null) {
            maintenance.setWorkshop(updateDto.workshop());
        }
        if (updateDto.technician() != null) {
            maintenance.setTechnician(updateDto.technician());
        }
        if (updateDto.notes() != null) {
            maintenance.setNotes(updateDto.notes());
        }

        Maintenance updated = maintenanceRepository.save(maintenance);
        System.out.println("✅ MaintenanceService: Maintenance updated successfully");
        
        return mapToDto(updated);
    }

    /**
     * Inicia uma manutenção (muda status para IN_PROGRESS)
     */
    public MaintenanceDto startMaintenance(Long id) {
        System.out.println("🚀 MaintenanceService: Starting maintenance ID: " + id);
        
        Maintenance maintenance = maintenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        if (maintenance.getStatus() != MaintenanceStatus.PLANNED) {
            throw new RuntimeException("Can only start planned maintenances");
        }

        maintenance.setStatus(MaintenanceStatus.IN_PROGRESS);
        maintenance.setStartDate(LocalDateTime.now());

        Maintenance updated = maintenanceRepository.save(maintenance);
        System.out.println("✅ MaintenanceService: Maintenance started successfully");
        
        return mapToDto(updated);
    }

    /**
     * Completa uma manutenção
     */
    public MaintenanceDto completeMaintenance(Long id, BigDecimal actualCost, String completionNotes) {
        System.out.println("🏁 MaintenanceService: Completing maintenance ID: " + id);
        
        Maintenance maintenance = maintenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        if (maintenance.getStatus() != MaintenanceStatus.IN_PROGRESS) {
            throw new RuntimeException("Can only complete in-progress maintenances");
        }

        maintenance.setStatus(MaintenanceStatus.COMPLETED);
        maintenance.setCompletionDate(LocalDateTime.now());
        maintenance.setActualCost(actualCost);
        
        if (completionNotes != null && !completionNotes.trim().isEmpty()) {
            String existingNotes = maintenance.getNotes() != null ? maintenance.getNotes() : "";
            maintenance.setNotes(existingNotes + "\n\nCompletion: " + completionNotes);
        }

        // Atualiza a data de última manutenção do veículo
        Vehicle vehicle = maintenance.getVehicle();
        vehicle.setLastMaintenanceDate(LocalDateTime.now());
        vehicleRepository.save(vehicle);

        Maintenance updated = maintenanceRepository.save(maintenance);
        System.out.println("✅ MaintenanceService: Maintenance completed successfully");
        
        return mapToDto(updated);
    }

    /**
     * Cancela uma manutenção
     */
    public MaintenanceDto cancelMaintenance(Long id, String reason) {
        System.out.println("❌ MaintenanceService: Cancelling maintenance ID: " + id);
        
        Maintenance maintenance = maintenanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        if (maintenance.getStatus() == MaintenanceStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed maintenance");
        }

        maintenance.setStatus(MaintenanceStatus.CANCELLED);
        
        if (reason != null && !reason.trim().isEmpty()) {
            String existingNotes = maintenance.getNotes() != null ? maintenance.getNotes() : "";
            maintenance.setNotes(existingNotes + "\n\nCancelled: " + reason);
        }

        Maintenance updated = maintenanceRepository.save(maintenance);
        System.out.println("✅ MaintenanceService: Maintenance cancelled successfully");
        
        return mapToDto(updated);
    }

    /**
     * Remove uma manutenção
     */
    public void deleteMaintenance(Long id) {
        System.out.println("🗑️ MaintenanceService: Deleting maintenance ID: " + id);
        
        if (!maintenanceRepository.existsById(id)) {
            throw new RuntimeException("Maintenance not found with id: " + id);
        }

        maintenanceRepository.deleteById(id);
        System.out.println("✅ MaintenanceService: Maintenance deleted successfully");
    }

    /**
     * Estatísticas de manutenção
     */
    public MaintenanceStatsDto getMaintenanceStats() {
        long totalMaintenances = maintenanceRepository.count();
        long plannedCount = maintenanceRepository.countByStatus(MaintenanceStatus.PLANNED);
        long inProgressCount = maintenanceRepository.countByStatus(MaintenanceStatus.IN_PROGRESS);
        long completedCount = maintenanceRepository.countByStatus(MaintenanceStatus.COMPLETED);
        long overdueCount = maintenanceRepository.findOverdueMaintenances(LocalDateTime.now()).size();
        
        BigDecimal totalCost = maintenanceRepository.findCompletedMaintenances()
            .stream()
            .map(Maintenance::getActualCost)
            .filter(cost -> cost != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MaintenanceStatsDto(
            totalMaintenances,
            plannedCount,
            inProgressCount,
            completedCount,
            overdueCount,
            totalCost
        );
    }

    // Métodos de mapeamento
    private MaintenanceDto mapToDto(Maintenance maintenance) {
        return new MaintenanceDto(
            maintenance.getId(),
            maintenance.getVehicle().getId(),
            maintenance.getVehicle().getLicensePlate(),
            maintenance.getDescription(),
            maintenance.getType(),
            maintenance.getStatus(),
            maintenance.getScheduledDate(),
            maintenance.getStartDate(),
            maintenance.getCompletionDate(),
            maintenance.getEstimatedCost(), // Using estimatedCost for the cost field
            maintenance.getTechnicianName(),
            maintenance.getWorkshopName(),
            maintenance.getNotes(),
            maintenance.getCreatedAt(),
            maintenance.getUpdatedAt()
        );
    }

    private MaintenanceSummaryDto mapToSummaryDto(Maintenance maintenance) {
        return new MaintenanceSummaryDto(
            maintenance.getId(),
            maintenance.getVehicle().getLicensePlate(),
            maintenance.getDescription(),
            maintenance.getType(),
            maintenance.getStatus(),
            maintenance.getScheduledDate(),
            maintenance.isOverdue()
        );
    }

    // DTO para estatísticas
    public record MaintenanceStatsDto(
        long totalMaintenances,
        long plannedCount,
        long inProgressCount,
        long completedCount,
        long overdueCount,
        BigDecimal totalCost
    ) {}
}