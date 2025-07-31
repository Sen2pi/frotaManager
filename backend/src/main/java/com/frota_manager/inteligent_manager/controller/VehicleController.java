package com.frota_manager.inteligent_manager.controller;

import com.frota_manager.inteligent_manager.dto.*;
import com.frota_manager.inteligent_manager.model.FuelType;
import com.frota_manager.inteligent_manager.model.VehicleStatus;
import com.frota_manager.inteligent_manager.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para Vehicle com todas as operações REST
 * Utiliza anotações Spring para mapeamento de endpoints e validação
 */
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {
    
    private final VehicleService vehicleService;
    
    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    
    /**
     * Cria um novo veículo
     */
    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@Valid @RequestBody CreateVehicleDto createDto) {
        VehicleDto createdVehicle = vehicleService.createVehicle(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
    }
    
    /**
     * Busca um veículo por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca um veículo por matrícula
     */
    @GetMapping("/license-plate/{licensePlate}")
    public ResponseEntity<VehicleDto> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return vehicleService.getVehicleByLicensePlate(licensePlate)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Lista todos os veículos
     */
    @GetMapping
    public ResponseEntity<List<VehicleDto>> getAllVehicles() {
        List<VehicleDto> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lista veículos disponíveis
     */
    @GetMapping("/available")
    public ResponseEntity<List<VehicleSummaryDto>> getAvailableVehicles() {
        List<VehicleSummaryDto> vehicles = vehicleService.getAvailableVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lista veículos por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VehicleDto>> getVehiclesByStatus(@PathVariable VehicleStatus status) {
        List<VehicleDto> vehicles = vehicleService.getVehiclesByStatus(status);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lista veículos que precisam de manutenção
     */
    @GetMapping("/needing-maintenance")
    public ResponseEntity<List<VehicleSummaryDto>> getVehiclesNeedingMaintenance() {
        List<VehicleSummaryDto> vehicles = vehicleService.getVehiclesNeedingMaintenance();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lista veículos com baixo nível de combustível
     */
    @GetMapping("/low-fuel")
    public ResponseEntity<List<VehicleSummaryDto>> getVehiclesWithLowFuel() {
        List<VehicleSummaryDto> vehicles = vehicleService.getVehiclesWithLowFuel();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lista veículos por marca
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<VehicleDto>> getVehiclesByBrand(@PathVariable String brand) {
        List<VehicleDto> vehicles = vehicleService.getVehiclesByBrand(brand);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lista veículos por modelo
     */
    @GetMapping("/model/{model}")
    public ResponseEntity<List<VehicleDto>> getVehiclesByModel(@PathVariable String model) {
        List<VehicleDto> vehicles = vehicleService.getVehiclesByModel(model);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lista veículos por tipo de combustível
     */
    @GetMapping("/fuel-type/{fuelType}")
    public ResponseEntity<List<VehicleDto>> getVehiclesByFuelType(@PathVariable FuelType fuelType) {
        List<VehicleDto> vehicles = vehicleService.getVehiclesByFuelType(fuelType);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Atualiza um veículo
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable Long id, @Valid @RequestBody UpdateVehicleDto updateDto) {
        try {
            VehicleDto updatedVehicle = vehicleService.updateVehicle(id, updateDto);
            return ResponseEntity.ok(updatedVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Atualiza o nível de combustível de um veículo
     */
    @PatchMapping("/{id}/fuel-level")
    public ResponseEntity<VehicleDto> updateFuelLevel(@PathVariable Long id, @RequestParam Double fuelLevel) {
        try {
            VehicleDto updatedVehicle = vehicleService.updateFuelLevel(id, fuelLevel);
            return ResponseEntity.ok(updatedVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza a quilometragem de um veículo
     */
    @PatchMapping("/{id}/kilometers")
    public ResponseEntity<VehicleDto> updateKilometers(@PathVariable Long id, @RequestParam Double kilometers) {
        try {
            VehicleDto updatedVehicle = vehicleService.updateKilometers(id, kilometers);
            return ResponseEntity.ok(updatedVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Deleta um veículo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 