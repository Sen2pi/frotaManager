package com.frota_manager.inteligent_manager.service;

import com.frota_manager.inteligent_manager.dto.*;
import com.frota_manager.inteligent_manager.model.*;
import com.frota_manager.inteligent_manager.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para Vehicle com toda a lógica de negócio
 * Utiliza anotações Spring para injeção de dependência e transações
 */
@Service
@Transactional
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;
    
    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    
    /**
     * Cria um novo veículo
     */
    public VehicleDto createVehicle(CreateVehicleDto createDto) {
        // Verifica se já existe veículo com a mesma matrícula
        if (vehicleRepository.existsByLicensePlate(createDto.licensePlate())) {
            throw new IllegalArgumentException("Já existe um veículo com a matrícula: " + createDto.licensePlate());
        }
        
        Vehicle vehicle = new Vehicle(
            createDto.brand(),
            createDto.model(),
            createDto.licensePlate(),
            createDto.year()
        );
        
        vehicle.setFuelType(createDto.fuelType());
        vehicle.setFuelCapacity(createDto.fuelCapacity());
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapToDto(savedVehicle);
    }
    
    /**
     * Atualiza um veículo existente
     */
    public VehicleDto updateVehicle(Long id, UpdateVehicleDto updateDto) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com ID: " + id));
        
        vehicle.setBrand(updateDto.brand());
        vehicle.setModel(updateDto.model());
        vehicle.setStatus(updateDto.status());
        vehicle.setFuelType(updateDto.fuelType());
        vehicle.setFuelCapacity(updateDto.fuelCapacity());
        vehicle.setCurrentFuelLevel(updateDto.currentFuelLevel());
        vehicle.setNextMaintenanceDate(updateDto.nextMaintenanceDate());
        
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToDto(updatedVehicle);
    }
    
    /**
     * Busca um veículo por ID
     */
    @Transactional(readOnly = true)
    public Optional<VehicleDto> getVehicleById(Long id) {
        return vehicleRepository.findById(id)
            .map(this::mapToDto);
    }
    
    /**
     * Busca um veículo por matrícula
     */
    @Transactional(readOnly = true)
    public Optional<VehicleDto> getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
            .map(this::mapToDto);
    }
    
    /**
     * Lista todos os veículos
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getAllVehicles() {
        return vehicleRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista veículos disponíveis
     */
    @Transactional(readOnly = true)
    public List<VehicleSummaryDto> getAvailableVehicles() {
        return vehicleRepository.findAvailableVehicles().stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista veículos por status
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista veículos que precisam de manutenção
     */
    @Transactional(readOnly = true)
    public List<VehicleSummaryDto> getVehiclesNeedingMaintenance() {
        return vehicleRepository.findVehiclesNeedingMaintenance(LocalDateTime.now()).stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista veículos com baixo nível de combustível
     */
    @Transactional(readOnly = true)
    public List<VehicleSummaryDto> getVehiclesWithLowFuel() {
        return vehicleRepository.findVehiclesWithLowFuel().stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista veículos por marca
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByBrand(String brand) {
        return vehicleRepository.findByBrandContainingIgnoreCase(brand).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista veículos por modelo
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByModel(String model) {
        return vehicleRepository.findByModelContainingIgnoreCase(model).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista veículos por tipo de combustível
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByFuelType(FuelType fuelType) {
        return vehicleRepository.findByFuelType(fuelType).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Deleta um veículo
     */
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Veículo não encontrado com ID: " + id);
        }
        vehicleRepository.deleteById(id);
    }
    
    /**
     * Atualiza o nível de combustível de um veículo
     */
    public VehicleDto updateFuelLevel(Long id, Double newFuelLevel) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com ID: " + id));
        
        if (vehicle.getFuelCapacity() != null && newFuelLevel > vehicle.getFuelCapacity()) {
            throw new IllegalArgumentException("Nível de combustível não pode exceder a capacidade");
        }
        
        vehicle.setCurrentFuelLevel(newFuelLevel);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToDto(updatedVehicle);
    }
    
    /**
     * Atualiza a quilometragem de um veículo
     */
    public VehicleDto updateKilometers(Long id, Double newKilometers) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado com ID: " + id));
        
        if (newKilometers < vehicle.getTotalKilometers()) {
            throw new IllegalArgumentException("Nova quilometragem não pode ser menor que a atual");
        }
        
        vehicle.setTotalKilometers(newKilometers);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToDto(updatedVehicle);
    }
    
    /**
     * Mapeia entidade para DTO
     */
    private VehicleDto mapToDto(Vehicle vehicle) {
        return new VehicleDto(
            vehicle.getId(),
            vehicle.getBrand(),
            vehicle.getModel(),
            vehicle.getLicensePlate(),
            vehicle.getYear(),
            vehicle.getStatus(),
            vehicle.getFuelType(),
            vehicle.getFuelCapacity(),
            vehicle.getCurrentFuelLevel(),
            vehicle.getTotalKilometers(),
            vehicle.getLastMaintenanceDate(),
            vehicle.getNextMaintenanceDate(),
            vehicle.getCreatedAt(),
            vehicle.getUpdatedAt()
        );
    }
    
    /**
     * Mapeia entidade para DTO de resumo
     */
    private VehicleSummaryDto mapToSummaryDto(Vehicle vehicle) {
        return new VehicleSummaryDto(
            vehicle.getId(),
            vehicle.getBrand(),
            vehicle.getModel(),
            vehicle.getLicensePlate(),
            vehicle.getStatus(),
            vehicle.getFuelPercentage(),
            vehicle.needsMaintenance()
        );
    }
} 