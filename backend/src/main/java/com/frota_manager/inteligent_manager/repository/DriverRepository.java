package com.frota_manager.inteligent_manager.repository;

import com.frota_manager.inteligent_manager.model.Driver;
import com.frota_manager.inteligent_manager.model.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para Driver com métodos de consulta personalizados
 * Utiliza Spring Data JPA para reduzir boilerplate code
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    
    /**
     * Encontra condutores por status
     */
    List<Driver> findByStatus(DriverStatus status);
    
    /**
     * Encontra condutores ativos
     */
    @Query("SELECT d FROM Driver d WHERE d.status = 'ACTIVE'")
    List<Driver> findActiveDrivers();
    
    /**
     * Encontra condutores por nome
     */
    List<Driver> findByNameContainingIgnoreCase(String name);
    
    /**
     * Encontra condutor por número de identificação
     */
    Optional<Driver> findByIdentificationNumber(String identificationNumber);
    
    /**
     * Encontra condutor por número da carta de condução
     */
    Optional<Driver> findByDriverLicenseNumber(String driverLicenseNumber);
    
    /**
     * Verifica se existe condutor com número de identificação
     */
    boolean existsByIdentificationNumber(String identificationNumber);
    
    /**
     * Verifica se existe condutor com número da carta de condução
     */
    boolean existsByDriverLicenseNumber(String driverLicenseNumber);
    
    /**
     * Encontra condutores com carta de condução a expirar
     */
    @Query("SELECT d FROM Driver d WHERE d.licenseExpiryDate <= :expiryDate")
    List<Driver> findDriversWithExpiringLicense(@Param("expiryDate") LocalDate expiryDate);
    
    /**
     * Encontra condutores com carta de condução expirada
     */
    @Query("SELECT d FROM Driver d WHERE d.licenseExpiryDate < :currentDate")
    List<Driver> findDriversWithExpiredLicense(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Encontra condutores por email
     */
    Optional<Driver> findByEmail(String email);
    
    /**
     * Encontra condutores por telefone
     */
    Optional<Driver> findByPhone(String phone);
    
    /**
     * Conta condutores por status
     */
    long countByStatus(DriverStatus status);
    
    /**
     * Encontra condutores com rating acima de um valor
     */
    @Query("SELECT d FROM Driver d WHERE d.rating >= :minRating")
    List<Driver> findDriversWithHighRating(@Param("minRating") Double minRating);
    
    /**
     * Encontra condutores com mais viagens que um valor
     */
    @Query("SELECT d FROM Driver d WHERE d.totalTrips >= :minTrips")
    List<Driver> findExperiencedDrivers(@Param("minTrips") Integer minTrips);
    
    /**
     * Encontra condutores com licença expirando antes de uma data
     */
    @Query("SELECT d FROM Driver d WHERE d.licenseExpiryDate < :date")
    List<Driver> findByLicenseExpiryDateBefore(@Param("date") LocalDate date);
} 