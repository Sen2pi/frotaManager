package com.frota_manager.inteligent_manager.service;

import com.frota_manager.inteligent_manager.dto.TripCreateDto;
import com.frota_manager.inteligent_manager.dto.TripDto;
import com.frota_manager.inteligent_manager.dto.TripSummaryDto;
import com.frota_manager.inteligent_manager.dto.TripUpdateDto;
import com.frota_manager.inteligent_manager.model.*;
import com.frota_manager.inteligent_manager.repository.TripRepository;
import com.frota_manager.inteligent_manager.repository.VehicleRepository;
import com.frota_manager.inteligent_manager.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de viagens
 * Implementa toda a lógica de negócio para o módulo de viagens
 */
@Service
@Transactional
public class TripService {

    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Autowired
    public TripService(TripRepository tripRepository, VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    /**
     * Cria uma nova viagem
     */
    public TripDto createTrip(TripCreateDto createDto) {
        System.out.println("🚗 TripService: Creating trip from " + createDto.origin() + " to " + createDto.destination());
        
        Vehicle vehicle = vehicleRepository.findById(createDto.vehicleId())
            .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + createDto.vehicleId()));

        Driver driver = driverRepository.findById(createDto.driverId())
            .orElseThrow(() -> new RuntimeException("Driver not found with id: " + createDto.driverId()));

        // Validações de negócio
        validateTripCreation(vehicle, driver, createDto.startTime());

        Trip trip = new Trip();
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setOrigin(createDto.origin());
        trip.setDestination(createDto.destination());
        trip.setStartTime(createDto.startTime());
        trip.setStatus(TripStatus.PLANNED);
        trip.setPurpose(createDto.purpose());
        trip.setNotes(createDto.notes());

        Trip saved = tripRepository.save(trip);
        System.out.println("✅ TripService: Trip created with ID: " + saved.getId());
        
        return mapToDto(saved);
    }

    /**
     * Busca viagem por ID
     */
    public Optional<TripDto> getTripById(Long id) {
        return tripRepository.findById(id)
            .map(this::mapToDto);
    }

    /**
     * Lista todas as viagens com paginação
     */
    public Page<TripSummaryDto> getAllTrips(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return tripRepository.findAll(pageable)
            .map(this::mapToSummaryDto);
    }

    /**
     * Busca viagens por veículo
     */
    public List<TripDto> getTripsByVehicle(Long vehicleId) {
        return tripRepository.findByVehicleIdOrderByStartTimeDesc(vehicleId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca viagens por condutor
     */
    public List<TripDto> getTripsByDriver(Long driverId) {
        return tripRepository.findByDriverIdOrderByStartTimeDesc(driverId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca viagens por status
     */
    public List<TripSummaryDto> getTripsByStatus(TripStatus status) {
        return tripRepository.findByStatusOrderByStartTimeDesc(status)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca viagens em andamento
     */
    public List<TripSummaryDto> getActiveTrips() {
        return tripRepository.findByStatusOrderByStartTimeDesc(TripStatus.IN_PROGRESS)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca viagens por período
     */
    public List<TripSummaryDto> getTripsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return tripRepository.findByStartTimeBetweenOrderByStartTimeDesc(startDate, endDate)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca viagens por distância
     */
    public List<TripSummaryDto> getTripsByDistance(Double minDistance, Double maxDistance) {
        return tripRepository.findByDistanceBetweenOrderByDistanceDesc(minDistance, maxDistance)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca viagens longas (mais de X km)
     */
    public List<TripSummaryDto> getLongTrips(Double minDistance) {
        return tripRepository.findByDistanceGreaterThanOrderByDistanceDesc(minDistance)
            .stream()
            .map(this::mapToSummaryDto)
            .collect(Collectors.toList());
    }

    /**
     * Atualiza uma viagem
     */
    public TripDto updateTrip(Long id, TripUpdateDto updateDto) {
        System.out.println("🚗 TripService: Updating trip ID: " + id);
        
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));

        // Atualiza campos se fornecidos
        if (updateDto.origin() != null) {
            trip.setOrigin(updateDto.origin());
        }
        if (updateDto.destination() != null) {
            trip.setDestination(updateDto.destination());
        }
        if (updateDto.startTime() != null) {
            trip.setStartTime(updateDto.startTime());
        }
        if (updateDto.status() != null) {
            trip.setStatus(updateDto.status());
            
            // Atualiza timestamps baseado no status
            if (updateDto.status() == TripStatus.IN_PROGRESS && trip.getStartTime() == null) {
                trip.setStartTime(LocalDateTime.now());
            }
            if (updateDto.status() == TripStatus.COMPLETED && trip.getEndTime() == null) {
                trip.setEndTime(LocalDateTime.now());
            }
        }
        if (updateDto.distance() != null) {
            trip.setDistanceKm(updateDto.distance());
        }
        if (updateDto.fuelConsumed() != null) {
            trip.setFuelConsumption(updateDto.fuelConsumed().doubleValue());
        }
        if (updateDto.purpose() != null) {
            trip.setPurpose(updateDto.purpose());
        }
        if (updateDto.notes() != null) {
            trip.setNotes(updateDto.notes());
        }

        Trip updated = tripRepository.save(trip);
        System.out.println("✅ TripService: Trip updated successfully");
        
        return mapToDto(updated);
    }

    /**
     * Inicia uma viagem (muda status para IN_PROGRESS)
     */
    public TripDto startTrip(Long id) {
        System.out.println("🚀 TripService: Starting trip ID: " + id);
        
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));

        if (trip.getStatus() != TripStatus.PLANNED) {
            throw new RuntimeException("Can only start planned trips");
        }

        // Validações adicionais
        Vehicle vehicle = trip.getVehicle();
        Driver driver = trip.getDriver();

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new RuntimeException("Vehicle is not available for trip");
        }

        if (driver.getStatus() != DriverStatus.ACTIVE) {
            throw new RuntimeException("Driver is not available for trip");
        }

        // Atualiza status da viagem, veículo e condutor
        trip.setStatus(TripStatus.IN_PROGRESS);
        if (trip.getStartTime() == null) {
            trip.setStartTime(LocalDateTime.now());
        }

        vehicle.setStatus(VehicleStatus.IN_USE);
        driver.setStatus(DriverStatus.ON_TRIP);

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);
        Trip updated = tripRepository.save(trip);

        System.out.println("✅ TripService: Trip started successfully");
        
        return mapToDto(updated);
    }

    /**
     * Finaliza uma viagem
     */
    public TripDto completeTrip(Long id, Double distance, BigDecimal fuelConsumed, String completionNotes) {
        System.out.println("🏁 TripService: Completing trip ID: " + id);
        
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new RuntimeException("Can only complete in-progress trips");
        }

        trip.setStatus(TripStatus.COMPLETED);
        trip.setEndTime(LocalDateTime.now());
        trip.setDistanceKm(distance);
        trip.setFuelConsumption(fuelConsumed.doubleValue());
        
        if (completionNotes != null && !completionNotes.trim().isEmpty()) {
            String existingNotes = trip.getNotes() != null ? trip.getNotes() : "";
            trip.setNotes(existingNotes + "\n\nCompletion: " + completionNotes);
        }

        // Atualiza status do veículo e condutor
        Vehicle vehicle = trip.getVehicle();
        Driver driver = trip.getDriver();

        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setTotalKilometers(vehicle.getTotalKilometers() + distance.intValue());
        
        // Reduz combustível se fornecido
        if (fuelConsumed != null && fuelConsumed.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentFuel = vehicle.getCurrentFuelLevel();
            BigDecimal newFuelLevel = currentFuel.subtract(fuelConsumed);
            if (newFuelLevel.compareTo(BigDecimal.ZERO) < 0) {
                newFuelLevel = BigDecimal.ZERO;
            }
            vehicle.setCurrentFuelLevel(newFuelLevel);
        }

        driver.setStatus(DriverStatus.ACTIVE);
        driver.setTotalTrips(driver.getTotalTrips() + 1);
        driver.setTotalKilometers(driver.getTotalKilometers() + distance.intValue());

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);
        Trip updated = tripRepository.save(trip);

        System.out.println("✅ TripService: Trip completed successfully");
        
        return mapToDto(updated);
    }

    /**
     * Cancela uma viagem
     */
    public TripDto cancelTrip(Long id, String reason) {
        System.out.println("❌ TripService: Cancelling trip ID: " + id);
        
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed trip");
        }

        TripStatus oldStatus = trip.getStatus();
        trip.setStatus(TripStatus.CANCELLED);
        
        if (reason != null && !reason.trim().isEmpty()) {
            String existingNotes = trip.getNotes() != null ? trip.getNotes() : "";
            trip.setNotes(existingNotes + "\n\nCancelled: " + reason);
        }

        // Se a viagem estava em andamento, libera veículo e condutor
        if (oldStatus == TripStatus.IN_PROGRESS) {
            Vehicle vehicle = trip.getVehicle();
            Driver driver = trip.getDriver();
            
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            driver.setStatus(DriverStatus.ACTIVE);
            
            vehicleRepository.save(vehicle);
            driverRepository.save(driver);
        }

        Trip updated = tripRepository.save(trip);
        System.out.println("✅ TripService: Trip cancelled successfully");
        
        return mapToDto(updated);
    }

    /**
     * Remove uma viagem
     */
    public void deleteTrip(Long id) {
        System.out.println("🗑️ TripService: Deleting trip ID: " + id);
        
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip not found with id: " + id);
        }

        tripRepository.deleteById(id);
        System.out.println("✅ TripService: Trip deleted successfully");
    }

    /**
     * Estatísticas de viagens
     */
    public TripStatsDto getTripStats() {
        long totalTrips = tripRepository.count();
        long plannedCount = tripRepository.countByStatus(TripStatus.PLANNED);
        long inProgressCount = tripRepository.countByStatus(TripStatus.IN_PROGRESS);
        long completedCount = tripRepository.countByStatus(TripStatus.COMPLETED);
        
        Double totalDistance = tripRepository.findCompletedTrips()
            .stream()
            .map(Trip::getDistance)
            .filter(distance -> distance != null)
            .reduce(0.0, Double::sum);

        BigDecimal totalFuelConsumed = tripRepository.findCompletedTrips()
            .stream()
            .map(Trip::getFuelConsumed)
            .filter(fuel -> fuel != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TripStatsDto(
            totalTrips,
            plannedCount,
            inProgressCount,
            completedCount,
            totalDistance,
            totalFuelConsumed
        );
    }

    /**
     * Validações para criação de viagem
     */
    private void validateTripCreation(Vehicle vehicle, Driver driver, LocalDateTime startTime) {
        // Verifica disponibilidade do veículo
        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE || vehicle.getStatus() == VehicleStatus.OUT_OF_SERVICE) {
            throw new RuntimeException("Vehicle is not available for trips");
        }

        // Verifica licença do condutor
        if (driver.getLicenseExpiryDate().isBefore(startTime.toLocalDate())) {
            throw new RuntimeException("Driver license is expired or will expire before trip");
        }

        // Verifica se há conflitos de horário
        List<Trip> conflictingTrips = tripRepository.findConflictingTrips(vehicle.getId(), driver.getId(), startTime);
        if (!conflictingTrips.isEmpty()) {
            throw new RuntimeException("Vehicle or driver has conflicting trip at this time");
        }
    }

    // Métodos de mapeamento
    private TripDto mapToDto(Trip trip) {
        return new TripDto(
            trip.getId(),
            trip.getVehicle().getId(),
            trip.getVehicle().getLicensePlate(),
            trip.getVehicle().getBrand() + " " + trip.getVehicle().getModel(),
            trip.getDriver().getId(),
            trip.getDriver().getName(),
            trip.getOrigin(),
            trip.getDestination(),
            trip.getStartTime(),
            trip.getActualStartTime(),
            trip.getEndTime(),
            trip.getStatus(),
            trip.getDistance(),
            trip.getFuelConsumed(),
            trip.getPurpose(),
            trip.getNotes()
        );
    }

    private TripSummaryDto mapToSummaryDto(Trip trip) {
        return new TripSummaryDto(
            trip.getId(),
            trip.getVehicle().getLicensePlate(),
            trip.getDriver().getName(),
            trip.getOrigin(),
            trip.getDestination(),
            trip.getStartTime(),
            trip.getStatus(),
            trip.getDistance()
        );
    }

    // DTO para estatísticas
    public record TripStatsDto(
        long totalTrips,
        long plannedCount,
        long inProgressCount,
        long completedCount,
        Double totalDistance,
        BigDecimal totalFuelConsumed
    ) {}
}