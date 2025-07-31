package com.frota_manager.inteligent_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma manutenção realizada num veículo
 * Utiliza anotações JPA para mapeamento objeto-relacional
 */
@Entity
@Table(name = "maintenances")
public class Maintenance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "O veículo é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @Column(name = "description", nullable = false)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MaintenanceType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MaintenanceStatus status = MaintenanceStatus.PLANNED;
    
    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    
    @Positive(message = "O custo deve ser positivo")
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;
    
    @Column(name = "technician_name")
    private String technicianName;
    
    @Column(name = "workshop_name")
    private String workshopName;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Construtores
    public Maintenance() {}
    
    public Maintenance(Vehicle vehicle, String description, MaintenanceType type) {
        this.vehicle = vehicle;
        this.description = description;
        this.type = type;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public MaintenanceType getType() { return type; }
    public void setType(MaintenanceType type) { this.type = type; }
    
    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }
    
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDateTime completionDate) { this.completionDate = completionDate; }
    
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    
    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }
    
    public String getWorkshopName() { return workshopName; }
    public void setWorkshopName(String workshopName) { this.workshopName = workshopName; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos de negócio
    public boolean isCompleted() {
        return status == MaintenanceStatus.COMPLETED;
    }
    
    public boolean isInProgress() {
        return status == MaintenanceStatus.IN_PROGRESS;
    }
    
    public boolean isOverdue() {
        return scheduledDate != null && 
               scheduledDate.isBefore(LocalDateTime.now()) && 
               status != MaintenanceStatus.COMPLETED;
    }
    
    public Long getDurationInHours() {
        if (startDate != null && completionDate != null) {
            return java.time.Duration.between(startDate, completionDate).toHours();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Maintenance{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
} 