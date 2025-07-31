package com.frota_manager.inteligent_manager.service;

import com.frota_manager.inteligent_manager.dto.*;
import com.frota_manager.inteligent_manager.model.*;
import com.frota_manager.inteligent_manager.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para Driver com toda a lógica de negócio
 * Utiliza anotações Spring para injeção de dependência e transações
 */
@Service
@Transactional
public class DriverService {
    
    private final DriverRepository driverRepository;
    
    @Autowired
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }
    
    /**
     * Cria um novo condutor
     */
    public DriverDto createDriver(CreateDriverDto createDto) {
        // Verifica se já existe condutor com o mesmo número de identificação
        if (driverRepository.existsByIdentificationNumber(createDto.identificationNumber())) {
            throw new IllegalArgumentException("Já existe um condutor com o número de identificação: " + createDto.identificationNumber());
        }
        
        // Verifica se já existe condutor com o mesmo número da carta de condução
        if (driverRepository.existsByDriverLicenseNumber(createDto.driverLicenseNumber())) {
            throw new IllegalArgumentException("Já existe um condutor com o número da carta de condução: " + createDto.driverLicenseNumber());
        }
        
        Driver driver = new Driver(
            createDto.name(),
            createDto.identificationNumber(),
            createDto.driverLicenseNumber(),
            createDto.licenseExpiryDate()
        );
        
        driver.setEmail(createDto.email());
        driver.setPhone(createDto.phone());
        
        Driver savedDriver = driverRepository.save(driver);
        return mapToDto(savedDriver);
    }
    
    /**
     * Atualiza um condutor existente
     */
    public DriverDto updateDriver(Long id, UpdateDriverDto updateDto) {
        Driver driver = driverRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Condutor não encontrado com ID: " + id));
        
        driver.setName(updateDto.name());
        driver.setEmail(updateDto.email());
        driver.setPhone(updateDto.phone());
        driver.setStatus(updateDto.status());
        
        Driver updatedDriver = driverRepository.save(driver);
        return mapToDto(updatedDriver);
    }
    
    /**
     * Busca um condutor por ID
     */
    @Transactional(readOnly = true)
    public Optional<DriverDto> getDriverById(Long id) {
        return driverRepository.findById(id)
            .map(this::mapToDto);
    }
    
    /**
     * Busca um condutor por número de identificação
     */
    @Transactional(readOnly = true)
    public Optional<DriverDto> getDriverByIdentificationNumber(String identificationNumber) {
        return driverRepository.findByIdentificationNumber(identificationNumber)
            .map(this::mapToDto);
    }
    
    /**
     * Busca um condutor por número da carta de condução
     */
    @Transactional(readOnly = true)
    public Optional<DriverDto> getDriverByLicenseNumber(String driverLicenseNumber) {
        return driverRepository.findByDriverLicenseNumber(driverLicenseNumber)
            .map(this::mapToDto);
    }
    
    /**
     * Lista todos os condutores
     */
    @Transactional(readOnly = true)
    public List<DriverDto> getAllDrivers() {
        return driverRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista condutores ativos
     */
    @Transactional(readOnly = true)
    public List<DriverSummaryDto> getActiveDrivers() {
        return driverRepository.findActiveDrivers().stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista condutores por status
     */
    @Transactional(readOnly = true)
    public List<DriverDto> getDriversByStatus(DriverStatus status) {
        return driverRepository.findByStatus(status).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista condutores com carta de condução a expirar
     */
    @Transactional(readOnly = true)
    public List<DriverSummaryDto> getDriversWithExpiringLicense() {
        LocalDate expiryDate = LocalDate.now().plusMonths(3);
        return driverRepository.findDriversWithExpiringLicense(expiryDate).stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista condutores com carta de condução expirada
     */
    @Transactional(readOnly = true)
    public List<DriverSummaryDto> getDriversWithExpiredLicense() {
        return driverRepository.findDriversWithExpiredLicense(LocalDate.now()).stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista condutores por nome
     */
    @Transactional(readOnly = true)
    public List<DriverDto> getDriversByName(String name) {
        return driverRepository.findByNameContainingIgnoreCase(name).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista condutores com alto rating
     */
    @Transactional(readOnly = true)
    public List<DriverSummaryDto> getDriversWithHighRating(Double minRating) {
        return driverRepository.findDriversWithHighRating(minRating).stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista condutores experientes
     */
    @Transactional(readOnly = true)
    public List<DriverSummaryDto> getExperiencedDrivers(Integer minTrips) {
        return driverRepository.findExperiencedDrivers(minTrips).stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Deleta um condutor
     */
    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new IllegalArgumentException("Condutor não encontrado com ID: " + id);
        }
        driverRepository.deleteById(id);
    }
    
    /**
     * Atualiza o rating de um condutor
     */
    public DriverDto updateDriverRating(Long id, Double newRating) {
        Driver driver = driverRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Condutor não encontrado com ID: " + id));
        
        if (newRating < 0.0 || newRating > 5.0) {
            throw new IllegalArgumentException("Rating deve estar entre 0.0 e 5.0");
        }
        
        driver.setRating(newRating);
        Driver updatedDriver = driverRepository.save(driver);
        return mapToDto(updatedDriver);
    }
    
    /**
     * Atualiza a data de validade da carta de condução
     */
    public DriverDto updateLicenseExpiryDate(Long id, LocalDate newExpiryDate) {
        Driver driver = driverRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Condutor não encontrado com ID: " + id));
        
        driver.setLicenseExpiryDate(newExpiryDate);
        Driver updatedDriver = driverRepository.save(driver);
        return mapToDto(updatedDriver);
    }
    
    /**
     * Mapeia entidade para DTO
     */
    private DriverDto mapToDto(Driver driver) {
        return new DriverDto(
            driver.getId(),
            driver.getName(),
            driver.getIdentificationNumber(),
            driver.getDriverLicenseNumber(),
            driver.getLicenseExpiryDate(),
            driver.getEmail(),
            driver.getPhone(),
            driver.getStatus(),
            driver.getTotalTrips(),
            driver.getTotalKilometers(),
            driver.getRating(),
            driver.getCreatedAt(),
            driver.getUpdatedAt()
        );
    }
    
    /**
     * Mapeia entidade para DTO de resumo
     */
    private DriverSummaryDto mapToSummaryDto(Driver driver) {
        return new DriverSummaryDto(
            driver.getId(),
            driver.getName(),
            driver.getDriverLicenseNumber(),
            driver.getStatus(),
            driver.isLicenseValid(),
            driver.isLicenseExpiringSoon(),
            driver.getRating()
        );
    }
} 