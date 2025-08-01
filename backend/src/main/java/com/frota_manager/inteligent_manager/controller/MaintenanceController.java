package com.frota_manager.inteligent_manager.controller;

import com.frota_manager.inteligent_manager.dto.CreateMaintenanceDto;
import com.frota_manager.inteligent_manager.dto.MaintenanceDto;
import com.frota_manager.inteligent_manager.dto.MaintenanceSummaryDto;
import com.frota_manager.inteligent_manager.dto.UpdateMaintenanceDto;
import com.frota_manager.inteligent_manager.model.MaintenanceStatus;
import com.frota_manager.inteligent_manager.model.MaintenanceType;
import com.frota_manager.inteligent_manager.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para gerenciamento de manutenções
 * Fornece endpoints completos para o módulo de manutenção
 */
@RestController
@RequestMapping("/api/maintenances")
@CrossOrigin(origins = "*")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    /**
     * Cria uma nova manutenção
     * POST /api/maintenances
     */
    @PostMapping
    public ResponseEntity<MaintenanceDto> createMaintenance(@Valid @RequestBody CreateMaintenanceDto createDto) {
        try {
            MaintenanceDto maintenance = maintenanceService.createMaintenance(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(maintenance);
        } catch (Exception e) {
            System.err.println("❌ Error creating maintenance: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca manutenção por ID
     * GET /api/maintenances/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDto> getMaintenanceById(@PathVariable Long id) {
        return maintenanceService.getMaintenanceById(id)
            .map(maintenance -> ResponseEntity.ok().body(maintenance))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as manutenções com paginação e ordenação
     * GET /api/maintenances?page=0&size=10&sortBy=scheduledDate&sortDir=desc
     */
    @GetMapping
    public ResponseEntity<Page<MaintenanceSummaryDto>> getAllMaintenances(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "scheduledDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<MaintenanceSummaryDto> maintenances = maintenanceService.getAllMaintenances(page, size, sortBy, sortDir);
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Busca manutenções por veículo
     * GET /api/maintenances/vehicle/{vehicleId}
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<MaintenanceDto>> getMaintenancesByVehicle(@PathVariable Long vehicleId) {
        List<MaintenanceDto> maintenances = maintenanceService.getMaintenancesByVehicle(vehicleId);
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Busca manutenções por status
     * GET /api/maintenances/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceSummaryDto>> getMaintenancesByStatus(@PathVariable MaintenanceStatus status) {
        List<MaintenanceSummaryDto> maintenances = maintenanceService.getMaintenancesByStatus(status);
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Busca manutenções por tipo
     * GET /api/maintenances/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MaintenanceSummaryDto>> getMaintenancesByType(@PathVariable MaintenanceType type) {
        List<MaintenanceSummaryDto> maintenances = maintenanceService.getMaintenancesByType(type);
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Busca manutenções programadas para período
     * GET /api/maintenances/scheduled?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/scheduled")
    public ResponseEntity<List<MaintenanceSummaryDto>> getScheduledMaintenances(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<MaintenanceSummaryDto> maintenances = maintenanceService.getScheduledMaintenances(startDate, endDate);
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Busca manutenções vencidas
     * GET /api/maintenances/overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<MaintenanceSummaryDto>> getOverdueMaintenances() {
        List<MaintenanceSummaryDto> maintenances = maintenanceService.getOverdueMaintenances();
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Busca manutenções por faixa de custo
     * GET /api/maintenances/cost-range?minCost=100&maxCost=1000
     */
    @GetMapping("/cost-range")
    public ResponseEntity<List<MaintenanceSummaryDto>> getMaintenancesByCostRange(
            @RequestParam BigDecimal minCost,
            @RequestParam BigDecimal maxCost) {
        
        List<MaintenanceSummaryDto> maintenances = maintenanceService.getMaintenancesByCostRange(minCost, maxCost);
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Busca manutenções por oficina
     * GET /api/maintenances/workshop/{workshop}
     */
    @GetMapping("/workshop/{workshop}")
    public ResponseEntity<List<MaintenanceSummaryDto>> getMaintenancesByWorkshop(@PathVariable String workshop) {
        List<MaintenanceSummaryDto> maintenances = maintenanceService.getMaintenancesByWorkshop(workshop);
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Atualiza uma manutenção
     * PUT /api/maintenances/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDto> updateMaintenance(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMaintenanceDto updateDto) {
        
        try {
            MaintenanceDto maintenance = maintenanceService.updateMaintenance(id, updateDto);
            return ResponseEntity.ok(maintenance);
        } catch (RuntimeException e) {
            System.err.println("❌ Error updating maintenance: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ Error updating maintenance: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Inicia uma manutenção
     * PATCH /api/maintenances/{id}/start
     */
    @PatchMapping("/{id}/start")
    public ResponseEntity<MaintenanceDto> startMaintenance(@PathVariable Long id) {
        try {
            MaintenanceDto maintenance = maintenanceService.startMaintenance(id);
            return ResponseEntity.ok(maintenance);
        } catch (RuntimeException e) {
            System.err.println("❌ Error starting maintenance: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Completa uma manutenção
     * PATCH /api/maintenances/{id}/complete
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<MaintenanceDto> completeMaintenance(
            @PathVariable Long id,
            @RequestParam BigDecimal actualCost,
            @RequestParam(required = false) String completionNotes) {
        
        try {
            MaintenanceDto maintenance = maintenanceService.completeMaintenance(id, actualCost, completionNotes);
            return ResponseEntity.ok(maintenance);
        } catch (RuntimeException e) {
            System.err.println("❌ Error completing maintenance: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancela uma manutenção
     * PATCH /api/maintenances/{id}/cancel
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<MaintenanceDto> cancelMaintenance(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        
        try {
            MaintenanceDto maintenance = maintenanceService.cancelMaintenance(id, reason);
            return ResponseEntity.ok(maintenance);
        } catch (RuntimeException e) {
            System.err.println("❌ Error cancelling maintenance: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Remove uma manutenção
     * DELETE /api/maintenances/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        try {
            maintenanceService.deleteMaintenance(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("❌ Error deleting maintenance: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Estatísticas de manutenção
     * GET /api/maintenances/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<MaintenanceService.MaintenanceStatsDto> getMaintenanceStats() {
        MaintenanceService.MaintenanceStatsDto stats = maintenanceService.getMaintenanceStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Endpoint de teste
     * GET /api/maintenances/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("MaintenanceController está funcionando!");
    }
}