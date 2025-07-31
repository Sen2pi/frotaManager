package com.frota_manager.inteligent_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um veículo na frota
 * Utiliza anotações JPA para mapeamento objeto-relacional
 * Inclui validações para garantir integridade dos dados
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "A marca é obrigatória")
    @Size(min = 2, max = 50, message = "A marca deve ter entre 2 e 50 caracteres")
    @Column(name = "brand", nullable = false)
    private String brand;
    
    @NotBlank(message = "O modelo é obrigatório")
    @Size(min = 2, max = 50, message = "O modelo deve ter entre 2 e 50 caracteres")
    @Column(name = "model", nullable = false)
    private String model;
    
    @NotBlank(message = "A matrícula é obrigatória")
    @Pattern(regexp = "^[A-Z]{2}-[0-9]{2}-[A-Z]{2}$", message = "A matrícula deve seguir o formato XX-00-XX")
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;
    
    @NotNull(message = "O ano é obrigatório")
    @Column(name = "manufacture_year", nullable = false)
    private Integer year;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VehicleStatus status = VehicleStatus.AVAILABLE;
    
    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    
    @Column(name = "fuel_capacity")
    private Double fuelCapacity;
    
    @Column(name = "current_fuel_level")
    private Double currentFuelLevel;
    
    @Column(name = "total_kilometers")
    private Double totalKilometers = 0.0;
    
    @Column(name = "last_maintenance_date")
    private LocalDateTime lastMaintenanceDate;
    
    @Column(name = "next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relacionamentos
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Maintenance> maintenances = new ArrayList<>();
    
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trip> trips = new ArrayList<>();
    
    // Construtores
    public Vehicle() {}
    
    public Vehicle(String brand, String model, String licensePlate, Integer year) {
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
    
    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
    
    public Double getFuelCapacity() { return fuelCapacity; }
    public void setFuelCapacity(Double fuelCapacity) { this.fuelCapacity = fuelCapacity; }
    
    public Double getCurrentFuelLevel() { return currentFuelLevel; }
    public void setCurrentFuelLevel(Double currentFuelLevel) { this.currentFuelLevel = currentFuelLevel; }
    
    public Double getTotalKilometers() { return totalKilometers; }
    public void setTotalKilometers(Double totalKilometers) { this.totalKilometers = totalKilometers; }
    
    public LocalDateTime getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(LocalDateTime lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }
    
    public LocalDateTime getNextMaintenanceDate() { return nextMaintenanceDate; }
    public void setNextMaintenanceDate(LocalDateTime nextMaintenanceDate) { this.nextMaintenanceDate = nextMaintenanceDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Maintenance> getMaintenances() { return maintenances; }
    public void setMaintenances(List<Maintenance> maintenances) { this.maintenances = maintenances; }
    
    public List<Trip> getTrips() { return trips; }
    public void setTrips(List<Trip> trips) { this.trips = trips; }
    
    // Métodos de negócio
    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }
    
    public boolean needsMaintenance() {
        return nextMaintenanceDate != null && nextMaintenanceDate.isBefore(LocalDateTime.now());
    }
    
    public double getFuelPercentage() {
        if (fuelCapacity == null || fuelCapacity == 0) return 0.0;
        return (currentFuelLevel != null ? currentFuelLevel : 0.0) / fuelCapacity * 100;
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", status=" + status +
                '}';
    }
} 