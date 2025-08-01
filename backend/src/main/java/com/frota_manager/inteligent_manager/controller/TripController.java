package com.frota_manager.inteligent_manager.controller;

import com.frota_manager.inteligent_manager.dto.TripCreateDto;
import com.frota_manager.inteligent_manager.dto.TripDto;
import com.frota_manager.inteligent_manager.dto.TripSummaryDto;
import com.frota_manager.inteligent_manager.dto.TripUpdateDto;
import com.frota_manager.inteligent_manager.model.TripStatus;
import com.frota_manager.inteligent_manager.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para gerenciamento de viagens
 * Fornece endpoints completos para o módulo de viagens
 */
@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Cria uma nova viagem
     * POST /api/trips
     */
    @PostMapping
    public ResponseEntity<TripDto> createTrip(@Valid @RequestBody TripCreateDto createDto) {
        try {
            TripDto trip = tripService.createTrip(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(trip);
        } catch (Exception e) {
            System.err.println("❌ Error creating trip: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca viagem por ID
     * GET /api/trips/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripDto> getTripById(@PathVariable Long id) {
        return tripService.getTripById(id)
            .map(trip -> ResponseEntity.ok().body(trip))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as viagens com paginação e ordenação
     * GET /api/trips?page=0&size=10&sortBy=startTime&sortDir=desc
     */
    @GetMapping
    public ResponseEntity<Page<TripSummaryDto>> getAllTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<TripSummaryDto> trips = tripService.getAllTrips(page, size, sortBy, sortDir);
        return ResponseEntity.ok(trips);
    }

    /**
     * Busca viagens por veículo
     * GET /api/trips/vehicle/{vehicleId}
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<TripDto>> getTripsByVehicle(@PathVariable Long vehicleId) {
        List<TripDto> trips = tripService.getTripsByVehicle(vehicleId);
        return ResponseEntity.ok(trips);
    }

    /**
     * Busca viagens por condutor
     * GET /api/trips/driver/{driverId}
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<TripDto>> getTripsByDriver(@PathVariable Long driverId) {
        List<TripDto> trips = tripService.getTripsByDriver(driverId);
        return ResponseEntity.ok(trips);
    }

    /**
     * Busca viagens por status
     * GET /api/trips/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TripSummaryDto>> getTripsByStatus(@PathVariable TripStatus status) {
        List<TripSummaryDto> trips = tripService.getTripsByStatus(status);
        return ResponseEntity.ok(trips);
    }

    /**
     * Busca viagens ativas (em andamento)
     * GET /api/trips/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<TripSummaryDto>> getActiveTrips() {
        List<TripSummaryDto> trips = tripService.getActiveTrips();
        return ResponseEntity.ok(trips);
    }

    /**
     * Busca viagens por período
     * GET /api/trips/date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<TripSummaryDto>> getTripsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<TripSummaryDto> trips = tripService.getTripsByDateRange(startDate, endDate);
        return ResponseEntity.ok(trips);
    }

    /**
     * Busca viagens por faixa de distância
     * GET /api/trips/distance-range?minDistance=100&maxDistance=500
     */
    @GetMapping("/distance-range")
    public ResponseEntity<List<TripSummaryDto>> getTripsByDistance(
            @RequestParam Double minDistance,
            @RequestParam Double maxDistance) {
        
        List<TripSummaryDto> trips = tripService.getTripsByDistance(minDistance, maxDistance);
        return ResponseEntity.ok(trips);
    }

    /**
     * Busca viagens longas (mais de X km)
     * GET /api/trips/long-trips?minDistance=300
     */
    @GetMapping("/long-trips")
    public ResponseEntity<List<TripSummaryDto>> getLongTrips(@RequestParam Double minDistance) {
        List<TripSummaryDto> trips = tripService.getLongTrips(minDistance);
        return ResponseEntity.ok(trips);
    }

    /**
     * Atualiza uma viagem
     * PUT /api/trips/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TripDto> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripUpdateDto updateDto) {
        
        try {
            TripDto trip = tripService.updateTrip(id, updateDto);
            return ResponseEntity.ok(trip);
        } catch (RuntimeException e) {
            System.err.println("❌ Error updating trip: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ Error updating trip: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Inicia uma viagem
     * PATCH /api/trips/{id}/start
     */
    @PatchMapping("/{id}/start")
    public ResponseEntity<TripDto> startTrip(@PathVariable Long id) {
        try {
            TripDto trip = tripService.startTrip(id);
            return ResponseEntity.ok(trip);
        } catch (RuntimeException e) {
            System.err.println("❌ Error starting trip: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Finaliza uma viagem
     * PATCH /api/trips/{id}/complete
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TripDto> completeTrip(
            @PathVariable Long id,
            @RequestParam Double distance,
            @RequestParam BigDecimal fuelConsumed,
            @RequestParam(required = false) String completionNotes) {
        
        try {
            TripDto trip = tripService.completeTrip(id, distance, fuelConsumed, completionNotes);
            return ResponseEntity.ok(trip);
        } catch (RuntimeException e) {
            System.err.println("❌ Error completing trip: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancela uma viagem
     * PATCH /api/trips/{id}/cancel
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TripDto> cancelTrip(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        
        try {
            TripDto trip = tripService.cancelTrip(id, reason);
            return ResponseEntity.ok(trip);
        } catch (RuntimeException e) {
            System.err.println("❌ Error cancelling trip: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Remove uma viagem
     * DELETE /api/trips/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        try {
            tripService.deleteTrip(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("❌ Error deleting trip: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Estatísticas de viagens
     * GET /api/trips/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<TripService.TripStatsDto> getTripStats() {
        TripService.TripStatsDto stats = tripService.getTripStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Endpoint de teste
     * GET /api/trips/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("TripController está funcionando!");
    }
}