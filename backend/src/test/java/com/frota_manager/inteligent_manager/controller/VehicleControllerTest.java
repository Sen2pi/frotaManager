package com.frota_manager.inteligent_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frota_manager.inteligent_manager.dto.CreateVehicleDto;
import com.frota_manager.inteligent_manager.dto.UpdateVehicleDto;
import com.frota_manager.inteligent_manager.dto.VehicleDto;
import com.frota_manager.inteligent_manager.dto.VehicleSummaryDto;
import com.frota_manager.inteligent_manager.model.FuelType;
import com.frota_manager.inteligent_manager.model.VehicleStatus;
import com.frota_manager.inteligent_manager.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para VehicleController
 * Utiliza @WebMvcTest para testar apenas a camada web
 */
@WebMvcTest(value = VehicleController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class VehicleControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private VehicleService vehicleService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private VehicleDto testVehicleDto;
    private CreateVehicleDto createVehicleDto;
    private UpdateVehicleDto updateVehicleDto;
    
    @BeforeEach
    void setUp() {
        testVehicleDto = new VehicleDto(
            1L,
            "Toyota",
            "Corolla",
            "AB-12-CD",
            2020,
            VehicleStatus.AVAILABLE,
            FuelType.GASOLINE,
            50.0,
            30.0,
            1000.0,
            LocalDateTime.now(),
            LocalDateTime.now().plusMonths(6),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        createVehicleDto = new CreateVehicleDto(
            "Toyota",
            "Corolla",
            "AB-12-CD",
            2020,
            FuelType.GASOLINE,
            50.0
        );
        
        updateVehicleDto = new UpdateVehicleDto(
            "Toyota",
            "Corolla",
            VehicleStatus.AVAILABLE,
            FuelType.GASOLINE,
            50.0,
            30.0,
            LocalDateTime.now().plusMonths(6)
        );
    }
    
    @Test
    void createVehicle_Success() throws Exception {
        // Arrange
        when(vehicleService.createVehicle(any(CreateVehicleDto.class))).thenReturn(testVehicleDto);
        
        // Act & Assert
        mockMvc.perform(post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createVehicleDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.brand").value("Toyota"))
            .andExpect(jsonPath("$.model").value("Corolla"))
            .andExpect(jsonPath("$.licensePlate").value("AB-12-CD"));
        
        verify(vehicleService).createVehicle(any(CreateVehicleDto.class));
    }
    
    @Test
    void getVehicleById_Success() throws Exception {
        // Arrange
        when(vehicleService.getVehicleById(1L)).thenReturn(Optional.of(testVehicleDto));
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.brand").value("Toyota"))
            .andExpect(jsonPath("$.model").value("Corolla"));
        
        verify(vehicleService).getVehicleById(1L);
    }
    
    @Test
    void getVehicleById_NotFound() throws Exception {
        // Arrange
        when(vehicleService.getVehicleById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles/1"))
            .andExpect(status().isNotFound());
        
        verify(vehicleService).getVehicleById(1L);
    }
    
    @Test
    void getVehicleByLicensePlate_Success() throws Exception {
        // Arrange
        when(vehicleService.getVehicleByLicensePlate("AB-12-CD")).thenReturn(Optional.of(testVehicleDto));
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles/license-plate/AB-12-CD"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.licensePlate").value("AB-12-CD"));
        
        verify(vehicleService).getVehicleByLicensePlate("AB-12-CD");
    }
    
    @Test
    void getAllVehicles_Success() throws Exception {
        // Arrange
        List<VehicleDto> vehicles = Arrays.asList(testVehicleDto);
        when(vehicleService.getAllVehicles()).thenReturn(vehicles);
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].brand").value("Toyota"))
            .andExpect(jsonPath("$[0].model").value("Corolla"));
        
        verify(vehicleService).getAllVehicles();
    }
    
    @Test
    void getAvailableVehicles_Success() throws Exception {
        // Arrange
        VehicleSummaryDto summaryDto = new VehicleSummaryDto(
            1L, "Toyota", "Corolla", "AB-12-CD", VehicleStatus.AVAILABLE, 60.0, false
        );
        List<VehicleSummaryDto> vehicles = Arrays.asList(summaryDto);
        when(vehicleService.getAvailableVehicles()).thenReturn(vehicles);
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles/available"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].brand").value("Toyota"))
            .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
        
        verify(vehicleService).getAvailableVehicles();
    }
    
    @Test
    void getVehiclesByStatus_Success() throws Exception {
        // Arrange
        List<VehicleDto> vehicles = Arrays.asList(testVehicleDto);
        when(vehicleService.getVehiclesByStatus(VehicleStatus.AVAILABLE)).thenReturn(vehicles);
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles/status/AVAILABLE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
        
        verify(vehicleService).getVehiclesByStatus(VehicleStatus.AVAILABLE);
    }
    
    @Test
    void getVehiclesNeedingMaintenance_Success() throws Exception {
        // Arrange
        VehicleSummaryDto summaryDto = new VehicleSummaryDto(
            1L, "Toyota", "Corolla", "AB-12-CD", VehicleStatus.MAINTENANCE, 60.0, true
        );
        List<VehicleSummaryDto> vehicles = Arrays.asList(summaryDto);
        when(vehicleService.getVehiclesNeedingMaintenance()).thenReturn(vehicles);
        
        // Act & Assert
        mockMvc.perform(get("/api/vehicles/needing-maintenance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].needsMaintenance").value(true));
        
        verify(vehicleService).getVehiclesNeedingMaintenance();
    }
    
    @Test
    void updateVehicle_Success() throws Exception {
        // Arrange
        when(vehicleService.updateVehicle(eq(1L), any(UpdateVehicleDto.class))).thenReturn(testVehicleDto);
        
        // Act & Assert
        mockMvc.perform(put("/api/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVehicleDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.brand").value("Toyota"));
        
        verify(vehicleService).updateVehicle(eq(1L), any(UpdateVehicleDto.class));
    }
    
    @Test
    void updateVehicle_NotFound() throws Exception {
        // Arrange
        when(vehicleService.updateVehicle(eq(1L), any(UpdateVehicleDto.class)))
            .thenThrow(new IllegalArgumentException("Veículo não encontrado"));
        
        // Act & Assert
        mockMvc.perform(put("/api/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVehicleDto)))
            .andExpect(status().isNotFound());
        
        verify(vehicleService).updateVehicle(eq(1L), any(UpdateVehicleDto.class));
    }
    
    @Test
    void updateFuelLevel_Success() throws Exception {
        // Arrange
        when(vehicleService.updateFuelLevel(1L, 40.0)).thenReturn(testVehicleDto);
        
        // Act & Assert
        mockMvc.perform(patch("/api/vehicles/1/fuel-level")
                .param("fuelLevel", "40.0"))
            .andExpect(status().isOk());
        
        verify(vehicleService).updateFuelLevel(1L, 40.0);
    }
    
    @Test
    void updateFuelLevel_BadRequest() throws Exception {
        // Arrange
        when(vehicleService.updateFuelLevel(1L, 60.0))
            .thenThrow(new IllegalArgumentException("Nível de combustível não pode exceder a capacidade"));
        
        // Act & Assert
        mockMvc.perform(patch("/api/vehicles/1/fuel-level")
                .param("fuelLevel", "60.0"))
            .andExpect(status().isBadRequest());
        
        verify(vehicleService).updateFuelLevel(1L, 60.0);
    }
    
    @Test
    void deleteVehicle_Success() throws Exception {
        // Arrange
        doNothing().when(vehicleService).deleteVehicle(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/vehicles/1"))
            .andExpect(status().isNoContent());
        
        verify(vehicleService).deleteVehicle(1L);
    }
    
    @Test
    void deleteVehicle_NotFound() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Veículo não encontrado")).when(vehicleService).deleteVehicle(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/vehicles/1"))
            .andExpect(status().isNotFound());
        
        verify(vehicleService).deleteVehicle(1L);
    }
} 