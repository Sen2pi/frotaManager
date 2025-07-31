package com.frota_manager.inteligent_manager.controller;

import com.frota_manager.inteligent_manager.dto.*;
import com.frota_manager.inteligent_manager.model.DriverStatus;
import com.frota_manager.inteligent_manager.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller para Driver com todas as operações REST
 * Utiliza anotações Spring para mapeamento de endpoints e validação
 */
@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = "*")
public class DriverController {
    
    private final DriverService driverService;
    
    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    
    /**
     * Cria um novo condutor
     */
    @PostMapping
    public ResponseEntity<DriverDto> createDriver(@Valid @RequestBody CreateDriverDto createDto) {
        DriverDto createdDriver = driverService.createDriver(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDriver);
    }
    
    /**
     * Busca um condutor por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca um condutor por número de identificação
     */
    @GetMapping("/identification/{identificationNumber}")
    public ResponseEntity<DriverDto> getDriverByIdentificationNumber(@PathVariable String identificationNumber) {
        return driverService.getDriverByIdentificationNumber(identificationNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca um condutor por número da carta de condução
     */
    @GetMapping("/license/{driverLicenseNumber}")
    public ResponseEntity<DriverDto> getDriverByLicenseNumber(@PathVariable String driverLicenseNumber) {
        return driverService.getDriverByLicenseNumber(driverLicenseNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Lista todos os condutores
     */
    @GetMapping
    public ResponseEntity<List<DriverDto>> getAllDrivers() {
        List<DriverDto> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Lista condutores ativos
     */
    @GetMapping("/active")
    public ResponseEntity<List<DriverSummaryDto>> getActiveDrivers() {
        List<DriverSummaryDto> drivers = driverService.getActiveDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Lista condutores por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DriverDto>> getDriversByStatus(@PathVariable DriverStatus status) {
        List<DriverDto> drivers = driverService.getDriversByStatus(status);
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Lista condutores com carta de condução a expirar
     */
    @GetMapping("/expiring-license")
    public ResponseEntity<List<DriverSummaryDto>> getDriversWithExpiringLicense() {
        List<DriverSummaryDto> drivers = driverService.getDriversWithExpiringLicense();
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Lista condutores com carta de condução expirada
     */
    @GetMapping("/expired-license")
    public ResponseEntity<List<DriverSummaryDto>> getDriversWithExpiredLicense() {
        List<DriverSummaryDto> drivers = driverService.getDriversWithExpiredLicense();
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Lista condutores por nome
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<DriverDto>> getDriversByName(@PathVariable String name) {
        List<DriverDto> drivers = driverService.getDriversByName(name);
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Lista condutores com alto rating
     */
    @GetMapping("/high-rating")
    public ResponseEntity<List<DriverSummaryDto>> getDriversWithHighRating(@RequestParam(defaultValue = "4.0") Double minRating) {
        List<DriverSummaryDto> drivers = driverService.getDriversWithHighRating(minRating);
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Lista condutores experientes
     */
    @GetMapping("/experienced")
    public ResponseEntity<List<DriverSummaryDto>> getExperiencedDrivers(@RequestParam(defaultValue = "10") Integer minTrips) {
        List<DriverSummaryDto> drivers = driverService.getExperiencedDrivers(minTrips);
        return ResponseEntity.ok(drivers);
    }
    
    /**
     * Atualiza um condutor
     */
    @PutMapping("/{id}")
    public ResponseEntity<DriverDto> updateDriver(@PathVariable Long id, @Valid @RequestBody UpdateDriverDto updateDto) {
        try {
            DriverDto updatedDriver = driverService.updateDriver(id, updateDto);
            return ResponseEntity.ok(updatedDriver);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Atualiza o rating de um condutor
     */
    @PatchMapping("/{id}/rating")
    public ResponseEntity<DriverDto> updateDriverRating(@PathVariable Long id, @RequestParam Double rating) {
        try {
            DriverDto updatedDriver = driverService.updateDriverRating(id, rating);
            return ResponseEntity.ok(updatedDriver);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza a data de validade da carta de condução
     */
    @PatchMapping("/{id}/license-expiry")
    public ResponseEntity<DriverDto> updateLicenseExpiryDate(@PathVariable Long id, @RequestParam String expiryDate) {
        try {
            LocalDate newExpiryDate = LocalDate.parse(expiryDate);
            DriverDto updatedDriver = driverService.updateLicenseExpiryDate(id, newExpiryDate);
            return ResponseEntity.ok(updatedDriver);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Deleta um condutor
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        try {
            driverService.deleteDriver(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 