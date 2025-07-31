package com.frota_manager.inteligent_manager.repository;

import com.frota_manager.inteligent_manager.model.Trip;
import com.frota_manager.inteligent_manager.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para Trip com métodos de consulta personalizados
 * Utiliza Spring Data JPA para reduzir boilerplate code
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    /**
     * Encontra viagens por status
     */
    List<Trip> findByStatus(TripStatus status);
    
    /**
     * Encontra viagens por veículo
     */
    List<Trip> findByVehicleId(Long vehicleId);
    
    /**
     * Encontra viagens por condutor
     */
    List<Trip> findByDriverId(Long driverId);
    
    /**
     * Encontra viagens ativas
     */
    @Query("SELECT t FROM Trip t WHERE t.status = 'IN_PROGRESS'")
    List<Trip> findActiveTrips();
    
    /**
     * Encontra viagens concluídas
     */
    @Query("SELECT t FROM Trip t WHERE t.status = 'COMPLETED'")
    List<Trip> findCompletedTrips();
    
    /**
     * Encontra viagens por período
     */
    @Query("SELECT t FROM Trip t WHERE t.startTime BETWEEN :startDate AND :endDate")
    List<Trip> findTripsByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Encontra viagens por origem
     */
    List<Trip> findByOriginContainingIgnoreCase(String origin);
    
    /**
     * Encontra viagens por destino
     */
    List<Trip> findByDestinationContainingIgnoreCase(String destination);
    
    /**
     * Encontra viagens por propósito
     */
    List<Trip> findByPurposeContainingIgnoreCase(String purpose);
    
    /**
     * Encontra viagens com distância acima de um valor
     */
    @Query("SELECT t FROM Trip t WHERE t.distanceKm > :minDistance")
    List<Trip> findLongDistanceTrips(@Param("minDistance") Double minDistance);
    
    /**
     * Encontra viagens com alto consumo de combustível
     */
    @Query("SELECT t FROM Trip t WHERE t.fuelConsumption > :maxConsumption")
    List<Trip> findHighFuelConsumptionTrips(@Param("maxConsumption") Double maxConsumption);
    
    /**
     * Conta viagens por status
     */
    long countByStatus(TripStatus status);
    
    /**
     * Conta viagens por veículo
     */
    long countByVehicleId(Long vehicleId);
    
    /**
     * Conta viagens por condutor
     */
    long countByDriverId(Long driverId);
    
    /**
     * Calcula a distância total percorrida por veículo
     */
    @Query("SELECT SUM(t.distanceKm) FROM Trip t WHERE t.vehicle.id = :vehicleId AND t.status = 'COMPLETED'")
    Double getTotalDistanceByVehicle(@Param("vehicleId") Long vehicleId);
    
    /**
     * Calcula o consumo total de combustível por veículo
     */
    @Query("SELECT SUM(t.fuelConsumption) FROM Trip t WHERE t.vehicle.id = :vehicleId AND t.status = 'COMPLETED'")
    Double getTotalFuelConsumptionByVehicle(@Param("vehicleId") Long vehicleId);
    
    /**
     * Encontra viagens recentes
     */
    @Query("SELECT t FROM Trip t WHERE t.createdAt >= :since ORDER BY t.createdAt DESC")
    List<Trip> findRecentTrips(@Param("since") LocalDateTime since);
} 