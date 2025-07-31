package com.frota_manager.inteligent_manager.service;

import com.frota_manager.inteligent_manager.dto.CreateVehicleDto;
import com.frota_manager.inteligent_manager.dto.VehicleDto;
import com.frota_manager.inteligent_manager.dto.VehicleSummaryDto;
import com.frota_manager.inteligent_manager.model.FuelType;
import com.frota_manager.inteligent_manager.model.Vehicle;
import com.frota_manager.inteligent_manager.model.VehicleStatus;
import com.frota_manager.inteligent_manager.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para VehicleService
 * Utiliza JUnit 5 e Mockito para testar a lógica de negócio
 */
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    
    @Mock
    private VehicleRepository vehicleRepository;
    
    @InjectMocks
    private VehicleService vehicleService;
    
    private Vehicle testVehicle;
    private CreateVehicleDto createVehicleDto;
    
    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle("Toyota", "Corolla", "AB-12-CD", 2020);
        testVehicle.setId(1L);
        testVehicle.setFuelType(FuelType.GASOLINE);
        testVehicle.setFuelCapacity(50.0);
        testVehicle.setCurrentFuelLevel(30.0);
        testVehicle.setStatus(VehicleStatus.AVAILABLE);
        testVehicle.setCreatedAt(LocalDateTime.now());
        testVehicle.setUpdatedAt(LocalDateTime.now());
        
        createVehicleDto = new CreateVehicleDto(
            "Toyota",
            "Corolla",
            "AB-12-CD",
            2020,
            FuelType.GASOLINE,
            50.0
        );
    }
    
    @Test
    void createVehicle_Success() {
        // Arrange
        when(vehicleRepository.existsByLicensePlate("AB-12-CD")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
        
        // Act
        VehicleDto result = vehicleService.createVehicle(createVehicleDto);
        
        // Assert
        assertNotNull(result);
        assertEquals("Toyota", result.brand());
        assertEquals("Corolla", result.model());
        assertEquals("AB-12-CD", result.licensePlate());
        assertEquals(2020, result.year());
        assertEquals(FuelType.GASOLINE, result.fuelType());
        assertEquals(50.0, result.fuelCapacity());
        
        verify(vehicleRepository).existsByLicensePlate("AB-12-CD");
        verify(vehicleRepository).save(any(Vehicle.class));
    }
    
    @Test
    void createVehicle_DuplicateLicensePlate_ThrowsException() {
        // Arrange
        when(vehicleRepository.existsByLicensePlate("AB-12-CD")).thenReturn(true);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vehicleService.createVehicle(createVehicleDto)
        );
        
        assertEquals("Já existe um veículo com a matrícula: AB-12-CD", exception.getMessage());
        verify(vehicleRepository).existsByLicensePlate("AB-12-CD");
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
    
    @Test
    void getVehicleById_Success() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        
        // Act
        Optional<VehicleDto> result = vehicleService.getVehicleById(1L);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("Toyota", result.get().brand());
        assertEquals("Corolla", result.get().model());
        
        verify(vehicleRepository).findById(1L);
    }
    
    @Test
    void getVehicleById_NotFound_ReturnsEmpty() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act
        Optional<VehicleDto> result = vehicleService.getVehicleById(1L);
        
        // Assert
        assertTrue(result.isEmpty());
        verify(vehicleRepository).findById(1L);
    }
    
    @Test
    void getAllVehicles_Success() {
        // Arrange
        Vehicle vehicle2 = new Vehicle("Honda", "Civic", "EF-34-GH", 2021);
        vehicle2.setId(2L);
        
        List<Vehicle> vehicles = Arrays.asList(testVehicle, vehicle2);
        when(vehicleRepository.findAll()).thenReturn(vehicles);
        
        // Act
        List<VehicleDto> result = vehicleService.getAllVehicles();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).brand());
        assertEquals("Honda", result.get(1).brand());
        
        verify(vehicleRepository).findAll();
    }
    
    @Test
    void getAvailableVehicles_Success() {
        // Arrange
        Vehicle availableVehicle = new Vehicle("Toyota", "Corolla", "AB-12-CD", 2020);
        availableVehicle.setId(1L);
        availableVehicle.setStatus(VehicleStatus.AVAILABLE);
        
        List<Vehicle> availableVehicles = Arrays.asList(availableVehicle);
        when(vehicleRepository.findAvailableVehicles()).thenReturn(availableVehicles);
        
        // Act
        List<VehicleSummaryDto> result = vehicleService.getAvailableVehicles();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(VehicleStatus.AVAILABLE, result.get(0).status());
        
        verify(vehicleRepository).findAvailableVehicles();
    }
    
    @Test
    void getVehiclesByStatus_Success() {
        // Arrange
        List<Vehicle> availableVehicles = Arrays.asList(testVehicle);
        when(vehicleRepository.findByStatus(VehicleStatus.AVAILABLE)).thenReturn(availableVehicles);
        
        // Act
        List<VehicleDto> result = vehicleService.getVehiclesByStatus(VehicleStatus.AVAILABLE);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(VehicleStatus.AVAILABLE, result.get(0).status());
        
        verify(vehicleRepository).findByStatus(VehicleStatus.AVAILABLE);
    }
    
    @Test
    void updateFuelLevel_Success() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
        
        // Act
        VehicleDto result = vehicleService.updateFuelLevel(1L, 40.0);
        
        // Assert
        assertNotNull(result);
        verify(vehicleRepository).findById(1L);
        verify(vehicleRepository).save(any(Vehicle.class));
    }
    
    @Test
    void updateFuelLevel_VehicleNotFound_ThrowsException() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vehicleService.updateFuelLevel(1L, 40.0)
        );
        
        assertEquals("Veículo não encontrado com ID: 1", exception.getMessage());
        verify(vehicleRepository).findById(1L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
    
    @Test
    void updateFuelLevel_ExceedsCapacity_ThrowsException() {
        // Arrange
        testVehicle.setFuelCapacity(50.0);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vehicleService.updateFuelLevel(1L, 60.0)
        );
        
        assertEquals("Nível de combustível não pode exceder a capacidade", exception.getMessage());
        verify(vehicleRepository).findById(1L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
    
    @Test
    void deleteVehicle_Success() {
        // Arrange
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        
        // Act
        vehicleService.deleteVehicle(1L);
        
        // Assert
        verify(vehicleRepository).existsById(1L);
        verify(vehicleRepository).deleteById(1L);
    }
    
    @Test
    void deleteVehicle_VehicleNotFound_ThrowsException() {
        // Arrange
        when(vehicleRepository.existsById(1L)).thenReturn(false);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vehicleService.deleteVehicle(1L)
        );
        
        assertEquals("Veículo não encontrado com ID: 1", exception.getMessage());
        verify(vehicleRepository).existsById(1L);
        verify(vehicleRepository, never()).deleteById(any());
    }
} 