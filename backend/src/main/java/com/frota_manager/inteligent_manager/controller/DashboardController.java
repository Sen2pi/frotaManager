package com.frota_manager.inteligent_manager.controller;

import com.frota_manager.inteligent_manager.service.VehicleService;
import com.frota_manager.inteligent_manager.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller para Dashboard com métricas e analytics
 * Utiliza anotações Spring para mapeamento de endpoints
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private final VehicleService vehicleService;
    private final DriverService driverService;
    
    @Autowired
    public DashboardController(VehicleService vehicleService, DriverService driverService) {
        this.vehicleService = vehicleService;
        this.driverService = driverService;
    }
    
    /**
     * Obtém métricas gerais do dashboard
     */
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Métricas de veículos
        metrics.put("totalVehicles", vehicleService.getAllVehicles().size());
        metrics.put("availableVehicles", vehicleService.getAvailableVehicles().size());
        metrics.put("vehiclesNeedingMaintenance", vehicleService.getVehiclesNeedingMaintenance().size());
        metrics.put("vehiclesWithLowFuel", vehicleService.getVehiclesWithLowFuel().size());
        
        // Métricas de condutores
        metrics.put("totalDrivers", driverService.getAllDrivers().size());
        metrics.put("activeDrivers", driverService.getActiveDrivers().size());
        metrics.put("driversWithExpiringLicense", driverService.getDriversWithExpiringLicense().size());
        metrics.put("driversWithExpiredLicense", driverService.getDriversWithExpiredLicense().size());
        
        return ResponseEntity.ok(metrics);
    }
    
    /**
     * Obtém alertas do sistema
     */
    @GetMapping("/alerts")
    public ResponseEntity<Map<String, Object>> getSystemAlerts() {
        Map<String, Object> alerts = new HashMap<>();
        
        // Alertas de veículos
        alerts.put("vehiclesNeedingMaintenance", vehicleService.getVehiclesNeedingMaintenance());
        alerts.put("vehiclesWithLowFuel", vehicleService.getVehiclesWithLowFuel());
        
        // Alertas de condutores
        alerts.put("driversWithExpiringLicense", driverService.getDriversWithExpiringLicense());
        alerts.put("driversWithExpiredLicense", driverService.getDriversWithExpiredLicense());
        
        return ResponseEntity.ok(alerts);
    }
    
    /**
     * Obtém estatísticas por tipo de combustível
     */
    @GetMapping("/fuel-statistics")
    public ResponseEntity<Map<String, Object>> getFuelStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Estatísticas por tipo de combustível
        statistics.put("gasolineVehicles", vehicleService.getVehiclesByFuelType(com.frota_manager.inteligent_manager.model.FuelType.GASOLINE).size());
        statistics.put("dieselVehicles", vehicleService.getVehiclesByFuelType(com.frota_manager.inteligent_manager.model.FuelType.DIESEL).size());
        statistics.put("electricVehicles", vehicleService.getVehiclesByFuelType(com.frota_manager.inteligent_manager.model.FuelType.ELECTRIC).size());
        statistics.put("hybridVehicles", vehicleService.getVehiclesByFuelType(com.frota_manager.inteligent_manager.model.FuelType.HYBRID).size());
        
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Obtém estatísticas por status de veículos
     */
    @GetMapping("/vehicle-status-statistics")
    public ResponseEntity<Map<String, Object>> getVehicleStatusStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Estatísticas por status
        statistics.put("availableVehicles", vehicleService.getVehiclesByStatus(com.frota_manager.inteligent_manager.model.VehicleStatus.AVAILABLE).size());
        statistics.put("inUseVehicles", vehicleService.getVehiclesByStatus(com.frota_manager.inteligent_manager.model.VehicleStatus.IN_USE).size());
        statistics.put("maintenanceVehicles", vehicleService.getVehiclesByStatus(com.frota_manager.inteligent_manager.model.VehicleStatus.MAINTENANCE).size());
        statistics.put("outOfServiceVehicles", vehicleService.getVehiclesByStatus(com.frota_manager.inteligent_manager.model.VehicleStatus.OUT_OF_SERVICE).size());
        
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Obtém estatísticas por status de condutores
     */
    @GetMapping("/driver-status-statistics")
    public ResponseEntity<Map<String, Object>> getDriverStatusStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Estatísticas por status
        statistics.put("activeDrivers", driverService.getDriversByStatus(com.frota_manager.inteligent_manager.model.DriverStatus.ACTIVE).size());
        statistics.put("inactiveDrivers", driverService.getDriversByStatus(com.frota_manager.inteligent_manager.model.DriverStatus.INACTIVE).size());
        statistics.put("onTripDrivers", driverService.getDriversByStatus(com.frota_manager.inteligent_manager.model.DriverStatus.ON_TRIP).size());
        statistics.put("onLeaveDrivers", driverService.getDriversByStatus(com.frota_manager.inteligent_manager.model.DriverStatus.ON_LEAVE).size());
        
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Obtém condutores com melhor rating
     */
    @GetMapping("/top-drivers")
    public ResponseEntity<Map<String, Object>> getTopDrivers(@RequestParam(defaultValue = "5") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        result.put("topDrivers", driverService.getDriversWithHighRating(4.0));
        return ResponseEntity.ok(result);
    }
    
    /**
     * Obtém atividades recentes
     */
    @GetMapping("/recent-activities")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        // Simular atividades recentes
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("icon", "directions_car");
        activity1.put("title", "Nouveau véhicule ajouté");
        activity1.put("description", "Renault Clio 2023 - AB-123-CD");
        activity1.put("time", "Il y a 2 heures");
        activities.add(activity1);
        
        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("icon", "build");
        activity2.put("title", "Maintenance planifiée");
        activity2.put("description", "Peugeot 308 - Révision générale");
        activity2.put("time", "Il y a 4 heures");
        activities.add(activity2);
        
        Map<String, Object> activity3 = new HashMap<>();
        activity3.put("icon", "person");
        activity3.put("title", "Conducteur assigné");
        activity3.put("description", "Jean Dupont - Véhicule BMW X3");
        activity3.put("time", "Il y a 6 heures");
        activities.add(activity3);
        
        Map<String, Object> activity4 = new HashMap<>();
        activity4.put("icon", "warning");
        activity4.put("title", "Alerte maintenance");
        activity4.put("description", "Audi A4 - Huile moteur faible");
        activity4.put("time", "Il y a 8 heures");
        activities.add(activity4);
        
        return ResponseEntity.ok(activities);
    }
} 