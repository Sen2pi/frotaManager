package com.frota_manager.inteligent_manager.repository;

import com.frota_manager.inteligent_manager.model.Vehicle;
import com.frota_manager.inteligent_manager.model.VehicleStatus;
import com.frota_manager.inteligent_manager.model.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para Vehicle com métodos de consulta personalizados
 * Utiliza Spring Data JPA para reduzir boilerplate code
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    /**
     * Encontra veículos por status
     */
    List<Vehicle> findByStatus(VehicleStatus status);
    
    /**
     * Encontra veículos disponíveis
     */
    @Query("SELECT v FROM Vehicle v WHERE v.status = 'AVAILABLE'")
    List<Vehicle> findAvailableVehicles();
    
    /**
     * Encontra veículos por marca
     */
    List<Vehicle> findByBrandContainingIgnoreCase(String brand);
    
    /**
     * Encontra veículos por modelo
     */
    List<Vehicle> findByModelContainingIgnoreCase(String model);
    
    /**
     * Encontra veículos por tipo de combustível
     */
    List<Vehicle> findByFuelType(FuelType fuelType);
    
    /**
     * Encontra veículos que precisam de manutenção
     */
    @Query("SELECT v FROM Vehicle v WHERE v.nextMaintenanceDate <= :date")
    List<Vehicle> findVehiclesNeedingMaintenance(@Param("date") LocalDateTime date);
    
    /**
     * Encontra veículos com baixo nível de combustível
     */
    @Query("SELECT v FROM Vehicle v WHERE v.currentFuelLevel < (v.fuelCapacity * 0.2)")
    List<Vehicle> findVehiclesWithLowFuel();
    
    /**
     * Encontra veículo por matrícula
     */
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    /**
     * Verifica se existe veículo com matrícula
     */
    boolean existsByLicensePlate(String licensePlate);
    
    /**
     * Conta veículos por status
     */
    long countByStatus(VehicleStatus status);
    
    /**
     * Encontra veículos por ano
     */
    List<Vehicle> findByYear(Integer year);
    
    /**
     * Encontra veículos por intervalo de anos
     */
    @Query("SELECT v FROM Vehicle v WHERE v.year BETWEEN :startYear AND :endYear")
    List<Vehicle> findVehiclesByYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
    
    /**
     * Encontra veículos com quilometragem acima de um valor
     */
    @Query("SELECT v FROM Vehicle v WHERE v.totalKilometers > :minKilometers")
    List<Vehicle> findVehiclesWithHighKilometers(@Param("minKilometers") Double minKilometers);
    
    /**
     * Encontra veículos por nível de combustível
     */
    @Query("SELECT v FROM Vehicle v WHERE v.currentFuelLevel < :fuelLevel")
    List<Vehicle> findByCurrentFuelLevelLessThan(@Param("fuelLevel") BigDecimal fuelLevel);
} 